package com.bfit.recommand.service.impl;

import com.bfit.recommand.common.ApplicationStatusEnum;
import com.bfit.recommand.common.OrderStatusEnum;
import com.bfit.recommand.common.util.RandomUtils;
import com.bfit.recommand.data.entity.ApplicationInfo;
import com.bfit.recommand.data.entity.InstanceMessage;
import com.bfit.recommand.data.entity.ProjectInfo;
import com.bfit.recommand.data.entity.UserInfo;
import com.bfit.recommand.data.entity.UserProject;
import com.bfit.recommand.repo.ApplicationInfoRepo;
import com.bfit.recommand.repo.InstanceMessageRepository;
import com.bfit.recommand.repo.ProjectInfoRepository;
import com.bfit.recommand.repo.UserInfoRepository;
import com.bfit.recommand.repo.UserProjectRepository;
import com.bfit.recommand.service.IApplicationService;
import com.bfit.recommand.service.INeedsService;
import com.bfit.recommand.web.dto.HomeNeedsDto;
import com.bfit.recommand.web.dto.NeedsApplicationDetailsDto;
import com.bfit.recommand.web.dto.PersonalNeedsDto;
import com.bfit.recommand.web.dto.request.PublishNeedsRequest;
import com.bfit.recommand.web.dto.request.UserApplyNeedsRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.bfit.recommand.common.RelationTypeEnum.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class NeedsServiceImpl {

    private final ProjectInfoRepository projectInfoRepository;
    private final UserInfoRepository userInfoRepository;
    private final UserProjectRepository userProjectRepository;
    private final InstanceMessageRepository instanceMessageRepository;
    private final ApplicationInfoRepo applicationInfoRepo;
    private final IApplicationService applicationServiceImpl;

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
        List<String> reviewerList = instanceMessages.stream().map(InstanceMessage::getUserAddress).collect(Collectors.toList());
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
                            UserInfo imUser = imUserInfos.stream().filter(s -> e.getUserAddress().equals(s.getUserWallet())).findFirst().orElse(null);
                            return PersonalNeedsDto.MessageDto.builder()
                                    .message(e.getMessage())
                                    .name(imUser == null ? e.getUserAddress() : imUser.getUserName())
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
                .projectAddress(RandomUtils.genSnowflakeWithSuffix("0x"))
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

    public List<NeedsApplicationDetailsDto> getNeedsAppList(String projectId) {

        if (StringUtils.isBlank(projectId)){
            return Collections.emptyList();
        }
        List<ApplicationInfo> applicationInfos = applicationInfoRepo.queryListByProjectId(Long.parseLong(projectId));
        if (CollectionUtils.isEmpty(applicationInfos)){
            return Collections.emptyList();
        }

        List<InstanceMessage> instanceMessages = instanceMessageRepository.queryByProjectAddress(applicationInfos.get(0).getProjectAddress());
        List<String> reviewerList = instanceMessages.stream().map(InstanceMessage::getUserAddress).collect(Collectors.toList());
        List<UserInfo> imUserInfos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(reviewerList)){
            imUserInfos.addAll(userInfoRepository.queryByUserWalletList(reviewerList.stream().distinct().collect(Collectors.toList())));
        }

        return applicationInfos.stream().map(x -> {
                    List<InstanceMessage> messageList = instanceMessages.stream()
                            .filter(e -> e.getApplicationId().equals(x.getId()))
                            .collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(messageList)){
                        return null;
                    }
                    List<PersonalNeedsDto.MessageDto> messageDtoList =
                            messageList.stream().map(s -> {
                                        UserInfo imUser = imUserInfos.stream().filter(y -> s.getUserAddress().equals(y.getUserWallet())).findFirst().orElse(null);
                                        return PersonalNeedsDto.MessageDto.builder()
                                                .message(s.getMessage())
                                                .createTime(s.getDbCreateTime())
                                                .name(Objects.isNull(imUser) ? s.getUserAddress() : imUser.getUserName())
                                                .build();
                                    }
                            ).collect(Collectors.toList());

                    UserInfo userInfo = imUserInfos.stream().filter(y -> x.getReviewerAddress().equals(y.getUserWallet())).findFirst().orElse(null);
                    return NeedsApplicationDetailsDto.builder()
                            .applicationId(String.valueOf(x.getId()))
                            .messageDtoList(messageDtoList)
                            .applicationUserAvatar(Objects.isNull(userInfo)?"":userInfo.getAvatar())
                            .applicationUserName(Objects.isNull(userInfo)?x.getReviewerAddress():userInfo.getUserName())
                            .applicationStatus(x.getApplicationStatus())
                            .createTime(x.getDbCreateTime())
                            .build();
                    }
                )
                .collect(Collectors.toList());

    }

//    public List<Object> getRelatedNeeds(){
//
//        return CommonResult.ok();
//    }

    public Boolean joinNeeds(UserApplyNeedsRequest request){

        ProjectInfo projectInfo = projectInfoRepository.queryByProjectId(request.getProjectId());
        if (Objects.isNull(projectInfo) || OrderStatusEnum.checkUnApplicableStatus(projectInfo.getProjectStatus())){
            return false;
        }
        List<ApplicationInfo> applicationInfos = applicationInfoRepo.queryListByProjectId(request.getProjectId());
        if (CollectionUtils.isNotEmpty(applicationInfos)
                && applicationInfos.stream().anyMatch(x -> x.getApplicationStatus().equals(ApplicationStatusEnum.ACCEPTED.getCode()))){
            throw new RuntimeException("Project is Already in process, You cannot apply now.");
        }

        return applicationServiceImpl.joinApplication(request.getProjectId(), request.getWalletAddress(), request.getProjectAddress());
    }

    public Boolean acceptNeeds(UserApplyNeedsRequest request){

        ProjectInfo projectInfo = projectInfoRepository.queryByProjectId(request.getProjectId());
        if (Objects.isNull(projectInfo) || OrderStatusEnum.checkUnApplicableStatus(projectInfo.getProjectStatus())){
            return false;
        }

        projectInfo.setProjectStatus(OrderStatusEnum.ACCEPTED.getCode());

        if (!applicationServiceImpl.acceptApplication(request.getProjectId(), request.getWalletAddress(), request.getProjectAddress())){
            return false;
        }
        return projectInfoRepository.updateById(projectInfo);
    }

    public Boolean rejectNeeds(UserApplyNeedsRequest request){

        ProjectInfo projectInfo = projectInfoRepository.queryByProjectId(request.getProjectId());
        if (Objects.isNull(projectInfo) || OrderStatusEnum.checkUnApplicableStatus(projectInfo.getProjectStatus())){
            return false;
        }

        return applicationServiceImpl.rejectApplication(request.getProjectId(), request.getWalletAddress(), request.getProjectAddress());
    }

//    public Boolean stopNeeds(){
//        return CommonResult.ok(true);
//    }
//
    public Boolean completeNeeds(UserApplyNeedsRequest request){
        ProjectInfo projectInfo = projectInfoRepository.queryByProjectId(request.getProjectId());
        if (Objects.isNull(projectInfo) || !OrderStatusEnum.PROCESSING.getCode().equals(projectInfo.getProjectStatus())){
            return false;
        }
        projectInfo.setProjectStatus(OrderStatusEnum.COMPLETED.getCode());
        return projectInfoRepository.updateById(projectInfo);
    }

    public Boolean finalConfirmNeeds(UserApplyNeedsRequest request){
        ProjectInfo projectInfo = projectInfoRepository.queryByProjectId(request.getProjectId());
        if (Objects.isNull(projectInfo) || !OrderStatusEnum.COMPLETED.getCode().equals(projectInfo.getProjectStatus())){
            return false;
        }
        projectInfo.setProjectStatus(OrderStatusEnum.FINAL_CONFIRMED.getCode());
        return projectInfoRepository.updateById(projectInfo);
    }



}
