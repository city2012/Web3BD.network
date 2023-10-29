package com.bfit.recommand.service;

import com.bfit.recommand.common.util.JsonUtils;
import com.bfit.recommand.data.entity.UserInfo;
import com.bfit.recommand.repo.UserInfoRepository;
import com.bfit.recommand.web.dto.UserInfoDto;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

}
