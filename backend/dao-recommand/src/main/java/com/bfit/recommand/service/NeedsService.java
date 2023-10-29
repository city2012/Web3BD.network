package com.bfit.recommand.service;

import com.bfit.recommand.common.dto.CommonResult;
import com.bfit.recommand.data.entity.ProjectInfo;
import com.bfit.recommand.data.entity.UserInfo;
import com.bfit.recommand.data.entity.UserProject;
import com.bfit.recommand.repo.ProjectInfoRepository;
import com.bfit.recommand.repo.UserInfoRepository;
import com.bfit.recommand.web.dto.HomeNeedsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NeedsService {

    private final ProjectInfoRepository projectInfoRepository;
    private final UserInfoRepository userInfoRepository;

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

            return HomeNeedsDto.builder().needsName(x.getProjectName())
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

    public List<HomeNeedsDto> getUserNeeds(String userWallet){

        List<ProjectInfo> projectInfoList = projectInfoRepository.queryRecentListByIssuer(userWallet);
        if (null == projectInfoList || projectInfoList.size() == 0){
            return Collections.emptyList();
        }
        List<UserInfo> userInfos = userInfoRepository.queryByUserWalletList(Collections.singletonList(userWallet));

        return projectInfoList.stream().map(x -> {

            UserInfo userInfo = userInfos.stream().filter(e -> e.getUserWallet().equals(x.getIssuerAddress())).findFirst().orElse(null);
            if (Objects.isNull(userInfo)){
                return null;
            }

            return HomeNeedsDto.builder().needsName(x.getProjectName())
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
