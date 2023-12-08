package com.bfit.recommand.service;

import com.bfit.recommand.common.Constants;
import com.bfit.recommand.common.OrgLevelEnum;
import com.bfit.recommand.common.util.JsonUtils;
import com.bfit.recommand.data.entity.UserInfo;
import com.bfit.recommand.repo.UserInfoRepository;
import com.bfit.recommand.web.dto.UserInfoDto;
import com.bfit.recommand.web.dto.request.UserRegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
                .desc(userInfo.getDescription())
                .socialLinkList(JsonUtils.toObjList(userInfo.getSocialLinks(), UserInfo.SocialLinkDto.class))
                .build();
    }

    public Boolean register(UserRegisterRequest request){
        Date date = new Date();

        if (Objects.nonNull(request.getCheckbox()) && !request.getCheckbox()){
            return false;
        }

        UserInfo userInfo = UserInfo.builder()
                .userName(request.getUserName())
                .userEmail(request.getUserEmail())
                .userWallet(request.getUserWallet())
                .avatar(request.getLogo())
                .socialLinks(JsonUtils.toJsonHasNullKey(
                                UserInfo.SocialLinkDto.builder()
                                        .name("twitter")
                                        .link(request.getWebsite())
                                        .build()
                        )
                )
                .description(request.getDescription())
                .dbUpdateTime(date)
                .dbCreateTime(date)
                .build();

        if (Objects.nonNull(request.getPersonFlag()) && request.getPersonFlag()){
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

    public List<UserInfoDto> fetchOrganizationList(String walletAddress) {

        List<UserInfo> userInfos = userInfoRepository.queryExceptUser(walletAddress, OrgLevelEnum.ORGANIZATION.getCode());
        if (CollectionUtils.isEmpty(userInfos)){
            return Collections.emptyList();
        }
        return userInfos.stream().map(x->{
            return UserInfoDto.builder()
                    .userEmail(x.getUserEmail())
                    .userName(x.getUserName())
                    .avatar(x.getAvatar())
                    .userWallet(x.getUserWallet())
                    .socialLinkList(JsonUtils.toObjList(x.getSocialLinks(), UserInfo.SocialLinkDto.class))
                    .build();
        }).collect(Collectors.toList());
    }
}
