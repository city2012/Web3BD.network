package com.bfit.recommand.service;

import com.bfit.recommand.common.Constants;
import com.bfit.recommand.common.OrgLevelEnum;
import com.bfit.recommand.common.util.JsonUtils;
import com.bfit.recommand.data.entity.UserInfo;
import com.bfit.recommand.repo.UserInfoRepository;
import com.bfit.recommand.web.dto.UserInfoDto;
import com.bfit.recommand.web.dto.request.UserRegisterRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserInfoRepository userInfoRepository;

    public Boolean registerEmail(String email, String walletAddress){
        Date date = new Date();
        UserInfo userInfo = UserInfo.builder().userEmail(email).userWallet(walletAddress)
                        .dbUpdateTime(date).dbCreateTime(date)
                        .build();
        return userInfoRepository.saveOne(userInfo);
    }

    public UserInfoDto fetchUserInfo(String walletAddress){

        List<UserInfo> userInfos = userInfoRepository.queryByUserWalletList(Collections.singletonList(walletAddress));
        if (null == userInfos || userInfos.size() == 0){
            return null;
        }

        UserInfo userInfo = userInfos.get(0);
        return UserInfoDto.builder()
                .userEmail(userInfo.getUserEmail())
                .userName(userInfo.getUserName())
                .avatar(userInfo.getAvatar())
                .socialLinkList(JsonUtils.toList(userInfo.getSocialLinks(), new TypeReference<>() {}))
                .build();
    }

    public Boolean register(UserRegisterRequest request){
        Date date = new Date();

        if (!request.getCheckbox()){
            return false;
        }

        UserInfo userInfo = UserInfo.builder()
                .userEmail(request.getUserEmail())
                .userWallet(request.getUserWallet())
                .avatar(request.getAvatar())
                .socialLinks(JsonUtils.toStringWithNullKey(
                                UserInfo.SocialLinkDto.builder()
                                        .name("twitter")
                                        .link(request.getTwitter())
                                        .build()
                        )
                )
                .description(request.getDescription())
                .dbUpdateTime(date)
                .dbCreateTime(date)
                .build();

        if (request.getPersonFlag()){
            String orgId = request.getOrgId();
            if (StringUtils.isNotBlank(orgId) && StringUtils.isNotBlank(orgId.trim())){
                userInfo.setOrganizationId(Long.parseLong(orgId.trim()));
            }else {
                userInfo.setOrganizationId(Constants.DEFAULT_ORG_ID);
            }
            userInfo.setLevel(OrgLevelEnum.PERSON.getCode());
            return userInfoRepository.saveOne(userInfo);
        }

        userInfo.setLevel(OrgLevelEnum.ORGANIZATION.getCode());
        return userInfoRepository.saveOne(userInfo);
    }

}
