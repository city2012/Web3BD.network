package com.bfit.recommand.repo;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.bfit.recommand.data.entity.InstanceMessage;
import com.bfit.recommand.data.mapper.InstanceMessageMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class InstanceMessageRepository {

    private final InstanceMessageMapper instanceMessageMapper;

    public List<InstanceMessage> queryByProjectAddress(String projectAddress){

        if (StringUtils.isBlank(projectAddress)){
            return Collections.emptyList();
        }

        return new LambdaQueryChainWrapper<>(instanceMessageMapper)
                .eq(InstanceMessage::getProjectAddress, projectAddress)
                .list();
    }

    public List<InstanceMessage> queryListByProjectAddressList(List<String> projectAddressList){

        if (CollectionUtils.isEmpty(projectAddressList)){
            return Collections.emptyList();
        }

        return new LambdaQueryChainWrapper<>(instanceMessageMapper)
                .in(InstanceMessage::getProjectAddress, projectAddressList)
                .list();
    }


}
