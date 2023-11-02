package com.bfit.recommand.repo;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bfit.recommand.data.entity.UserInfo;
import com.bfit.recommand.data.mapper.UserInfoMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class UserInfoRepository extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoRepo {

    private final UserInfoMapper userInfoMapper;

    public List<UserInfo> queryAll() {
        return new LambdaQueryChainWrapper<>(userInfoMapper).list();
    }

    public boolean existUser(String userWallet){

        if (StringUtils.isBlank(userWallet)){
            return false;
        }

        return Objects.nonNull(new LambdaQueryChainWrapper<>(userInfoMapper)
                .eq(UserInfo::getUserWallet, userWallet)
                .last("limit 1")
                .one());
    }

    public List<UserInfo> queryByUserWalletList(List<String> userWalletList) {

        return new LambdaQueryChainWrapper<>(userInfoMapper)
                .in(UserInfo::getUserWallet, userWalletList)
                .list();
    }

    public Boolean insertBatch(List<UserInfo> entityList){

        if (CollectionUtils.isEmpty(entityList)){
            return true;
        }

        return saveBatch(entityList);
    }

    public Boolean saveOne(UserInfo entity){
        if (Objects.isNull(entity)){
            return false;
        }
        return userInfoMapper.insert(entity) == 1;
    }

    public List<UserInfo> queryExceptUser(String walletAddress, Integer orgLevel) {
        return new LambdaQueryChainWrapper<>(userInfoMapper)
                .eq(StringUtils.isNotBlank(walletAddress),UserInfo::getUserWallet,walletAddress)
                .eq(UserInfo::getLevel, orgLevel)
                .list();
    }

}
