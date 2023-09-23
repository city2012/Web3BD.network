package com.bfit.recommand.service;

import com.bfit.recommand.data.entity.UserInfo;
import com.bfit.recommand.repo.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

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

}
