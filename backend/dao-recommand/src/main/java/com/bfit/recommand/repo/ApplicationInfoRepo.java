package com.bfit.recommand.repo;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.bfit.recommand.data.entity.ApplicationInfo;
import com.bfit.recommand.data.mapper.ApplicationInfoMapper;
import com.bfit.recommand.web.dto.PersonalNeedsDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class ApplicationInfoRepo {

    private final ApplicationInfoMapper applicationInfoMapper;

    public List<ApplicationInfo> queryListByProjectIdList(List<Long> projectIdList){

        if (CollectionUtils.isEmpty(projectIdList)){
            return Collections.emptyList();
        }

        return new LambdaQueryChainWrapper<>(applicationInfoMapper)
                .in(ApplicationInfo::getProjectId, projectIdList)
                .list();
    }


    public List<ApplicationInfo> queryListByProjectId(Long projectId) {

        if (Objects.isNull(projectId)){
            return Collections.emptyList();
        }

        return new LambdaQueryChainWrapper<>(applicationInfoMapper)
                .in(ApplicationInfo::getProjectId, projectId)
                .list();

    }

    public Boolean saveOne(ApplicationInfo entity){

        if (Objects.isNull(entity)){
            return false;
        }

        return applicationInfoMapper.insert(entity) > 0;
    }

    public Boolean updateById(ApplicationInfo entity){

        if (Objects.isNull(entity)){
            return false;
        }

        return applicationInfoMapper.updateById(entity) > 0;
    }

}
