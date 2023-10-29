package com.bfit.recommand.service;

import com.bfit.recommand.common.OrderStatusEnum;
import com.bfit.recommand.common.util.RandomUtils;
import com.bfit.recommand.data.entity.InstanceMessage;
import com.bfit.recommand.data.entity.ProjectInfo;
import com.bfit.recommand.data.entity.UserInfo;
import com.bfit.recommand.data.entity.UserProject;
import com.bfit.recommand.repo.InstanceMessageRepository;
import com.bfit.recommand.repo.ProjectInfoRepository;
import com.bfit.recommand.repo.UserInfoRepository;
import com.bfit.recommand.repo.UserProjectRepository;
import com.bfit.recommand.web.dto.HomeNeedsDto;
import com.bfit.recommand.web.dto.PersonalNeedsDto;
import com.bfit.recommand.web.dto.request.PublishNeedsRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.bfit.recommand.common.RelationTypeEnum.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class NeedsService {

    private final ProjectInfoRepository projectInfoRepository;
    private final UserInfoRepository userInfoRepository;
    private final UserProjectRepository userProjectRepository;
    private final InstanceMessageRepository instanceMessageRepository;

    public List<HomeNeedsDto> getPublicNeeds(){

        List<ProjectInfo> projectInfoList = projectInfoRepository.queryAll();
        Map<Long, String> issuerProjectMap =
                projectInfoList.stream().collect(Collectors.toMap(ProjectInfo::getId, ProjectInfo::getIssuerAddress));
        List<UserInfo> userInfos =
                userInfoRepository.queryByUserWalletList(new ArrayList<>(new HashSet<>(issuerProjectMap.values())));

        return projectInfoList.stream().map(x -> {

            UserInfo userInfo = userInfos.stream().filter(e -> e.getUserWallet().equals(x.getIssuerAddress())).findFirst().orElse(null);
            if (Objects.isNull(userInfo)){
                return null;
            }

            return HomeNeedsDto.builder()
                    .crypto(x.getProjectAsset())
                    .reward(x.getProjectPrice())
                    .needsName(x.getProjectName())
                    .avatar(userInfo.getAvatar())
                    .organizationName(userInfo.getUserName())
                    .description(x.getDescription())
                    .projectStatus(String.valueOf(x.getProjectStatus()))
                    .projectTag(x.getProjectTag())
                    .createTime(x.getDbCreateTime())
                    .lastModifyTime(x.getDbUpdateTime())
                    .build();

        }).collect(Collectors.toList());
    }

    public List<PersonalNeedsDto> getUserNeeds(String userWallet, Integer relationType){

        List<ProjectInfo> projectInfoList = fetchUserProjectListByRelation(userWallet, relationType);
        if (CollectionUtils.isEmpty(projectInfoList)){
            return Collections.emptyList();
        }
        List<UserInfo> userInfos = userInfoRepository.queryByUserWalletList(Collections.singletonList(userWallet));
        List<InstanceMessage> instanceMessages =
                instanceMessageRepository.queryListByProjectAddressList(projectInfoList.stream().map(ProjectInfo::getProjectAddress).collect(Collectors.toList()));
        List<String> reviewerList = instanceMessages.stream().map(InstanceMessage::getReviewerAddress).collect(Collectors.toList());
        List<UserInfo> imUserInfos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(reviewerList)){
            imUserInfos.addAll(userInfoRepository.queryByUserWalletList(reviewerList.stream().distinct().collect(Collectors.toList())));
        }

        return projectInfoList.stream().map(x -> {

            UserInfo userInfo = userInfos.stream().filter(e -> e.getUserWallet().equals(x.getIssuerAddress())).findFirst().orElse(null);
            if (Objects.isNull(userInfo)){
                return null;
            }

            return PersonalNeedsDto.builder().needsName(x.getProjectName())
                    .avatar(userInfo.getAvatar())
                    .organizationName(userInfo.getUserName())
                    .description(x.getDescription())
                    .projectStatus(String.valueOf(x.getProjectStatus()))
                    .projectTag(x.getProjectTag())
                    .messageList(fetchMessageInfos(instanceMessages, imUserInfos, x.getProjectAddress()))
                    .crypto(x.getProjectAsset())
                    .reward(x.getProjectPrice())
                    .createTime(x.getDbCreateTime())
                    .lastModifyTime(x.getDbUpdateTime())
                    .build();
        }).collect(Collectors.toList());
    }

    @NotNull
    private List<PersonalNeedsDto.MessageDto> fetchMessageInfos(List<InstanceMessage> totalMessageList, List<UserInfo> imUserInfos, String projectAddress) {
        if (CollectionUtils.isEmpty(totalMessageList)){
            return Collections.emptyList();
        }
        List<InstanceMessage> messageList = totalMessageList.stream().filter(e -> e.getProjectAddress().equals(projectAddress)).collect(Collectors.toList());

        return messageList.stream().map(e -> {
                            UserInfo imUser = imUserInfos.stream().filter(s -> e.getReviewerAddress().equals(s.getUserWallet())).findFirst().orElse(null);
                            return PersonalNeedsDto.MessageDto.builder()
                                    .message(e.getMessage())
                                    .name(imUser == null ? e.getReviewerAddress() : imUser.getUserName())
                                    .createTime(e.getDbCreateTime())
                                    .build();
                        }
                )
                .collect(Collectors.toList());
    }

    private List<ProjectInfo> fetchUserProjectListByRelation(String userWallet, Integer relationType){
        switch (fetchByCode(relationType)){
            case POST:
                return projectInfoRepository.queryListByIssuer(userWallet);
            case JOIN:{
                List<UserProject> userProjects = userProjectRepository.queryListByReviewerList(Collections.singletonList(userWallet));
                return projectInfoRepository.queryListByProjectAddressList(userProjects.stream().map(UserProject::getProjectAddress).collect(Collectors.toList()), OrderStatusEnum.APPLIED.getCode());
            }
            case RELATED: {
                List<UserProject> userProjects = userProjectRepository.queryListByReviewerList(Collections.singletonList(userWallet));
                return projectInfoRepository.queryListByProjectAddressList(userProjects.stream().map(UserProject::getProjectAddress).collect(Collectors.toList()), null);
            }
            default:
                return null;
        }
    }

    public Boolean publishNeeds(PublishNeedsRequest request){

        ProjectInfo projectInfo = ProjectInfo.builder()
                .projectName(request.getTitle())
                .description(request.getDescription())
                .projectAsset(request.getCrypto())
                .projectPrice(request.getReward())
                .projectStatus(OrderStatusEnum.INIT.getCode())
                .projectType(request.getType())
                .expireTime(request.getEndTime())
                .startTime(request.getStartTime())
                .issuerAddress(request.getUserWallet())
                .build();

        boolean projectInitedFlag = projectInfoRepository.saveOne(projectInfo);
        if (projectInitedFlag
                && !userInfoRepository.existUser(request.getUserWallet())){

            String userName = "Biz-" + RandomUtils.generateRandomString();
            UserInfo userInfo = UserInfo.builder()
                    .userWallet(request.getUserWallet())
                    .userName(userName)
                    .userEmail(userName + "@biz.com")
                    .build();
            return userInfoRepository.saveOne(userInfo);
        }
        return projectInitedFlag;
    }

//    public List<Object> getRelatedNeeds(){
//
//        return CommonResult.ok();
//    }
//
//    public Boolean joinNeeds(){
//        return CommonResult.ok(true);
//    }
//
//    public Boolean acceptNeeds(){
//        return CommonResult.ok(true);
//    }
//
//    public Boolean rejectNeeds(){
//        return CommonResult.ok(true);
//    }
//
//    public Boolean stopNeeds(){
//        return CommonResult.ok(true);
//    }
//
//    public Boolean completeNeeds(){
//        return CommonResult.ok(true);
//    }
//
//    public Boolean finalConfirmNeeds(){
//        return CommonResult.ok(true);
//    }



}
