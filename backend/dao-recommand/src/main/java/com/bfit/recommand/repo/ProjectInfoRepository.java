package com.bfit.recommand.repo;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.bfit.recommand.data.entity.ProjectInfo;
import com.bfit.recommand.data.mapper.ProjectInfoMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class ProjectInfoRepository {

    private final ProjectInfoMapper projectInfoMapper;

    public List<ProjectInfo> queryAll(){
        return new LambdaQueryChainWrapper<>(projectInfoMapper).list();
    }

    public ProjectInfo queryByProjectAddress(String projectAddress){
        return new LambdaQueryChainWrapper<>(projectInfoMapper)
                .eq(ProjectInfo::getProjectAddress, projectAddress)
                .last("limit 1").one();
    }

    public ProjectInfo queryByProjectId(Long projectId){
        return new LambdaQueryChainWrapper<>(projectInfoMapper)
                .eq(ProjectInfo::getId, projectId)
                .last("limit 1").one();
    }

    public List<ProjectInfo> queryRecentListExcludeIssuer(String issuerAddress){
        return new LambdaQueryChainWrapper<>(projectInfoMapper)
                .ne(ProjectInfo::getIssuerAddress, issuerAddress)
                .orderByDesc(ProjectInfo::getDbCreateTime)
                .list();
    }

    public List<ProjectInfo> queryListByIssuer(String issuerAddress){
        return new LambdaQueryChainWrapper<>(projectInfoMapper)
                .in(ProjectInfo::getIssuerAddress, issuerAddress)
                .orderByDesc(ProjectInfo::getDbCreateTime)
                .list();
    }

    public List<ProjectInfo> queryListByProjectAddressList(List<String> projectAddressList, Integer projectStatus){

        if (CollectionUtils.isEmpty(projectAddressList)){
            return Collections.emptyList();
        }

        return new LambdaQueryChainWrapper<>(projectInfoMapper)
                .in(ProjectInfo::getProjectAddress, projectAddressList)
                .eq(Objects.nonNull(projectStatus), ProjectInfo::getProjectStatus, projectStatus)
                .list();
    }

    public boolean saveOne(ProjectInfo entity){
        if (Objects.isNull(entity)){
            return false;
        }
        return projectInfoMapper.insert(entity) > 0;
    }

    public boolean updateById(ProjectInfo entity){
        if (Objects.isNull(entity)){
            return false;
        }
        return projectInfoMapper.updateById(entity) > 0;
    }

}
