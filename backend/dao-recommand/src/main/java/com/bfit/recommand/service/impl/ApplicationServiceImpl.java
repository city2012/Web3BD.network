package com.bfit.recommand.service.impl;

import com.bfit.recommand.common.ApplicationStatusEnum;
import com.bfit.recommand.data.entity.ApplicationInfo;
import com.bfit.recommand.repo.ApplicationInfoRepo;
import com.bfit.recommand.service.IApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApplicationServiceImpl implements IApplicationService {

    private final ApplicationInfoRepo applicationInfoRepo;

    @Override
    public Boolean joinApplication(Long projectId, String userWallet, String projectAddress) {

        ApplicationInfo applicationInfo = ApplicationInfo.builder()
                .projectId(projectId)
                .reviewerAddress(userWallet)
                .applicationStatus(ApplicationStatusEnum.APPLIED.getCode())
                .build();

        return applicationInfoRepo.saveOne(applicationInfo);
    }

    @Override
    public Boolean acceptApplication(Long projectId, String userWallet, String projectAddress) {

        List<ApplicationInfo> applicationInfos = applicationInfoRepo.queryListByProjectId(projectId);
        ApplicationInfo applicationInfo = applicationInfos.stream()
                .filter(x -> ApplicationStatusEnum.APPLIED.getCode().equals(x.getApplicationStatus()) && x.getReviewerAddress().equals(userWallet))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(applicationInfo)){
            return false;
        }
        applicationInfo.setApplicationStatus(ApplicationStatusEnum.ACCEPTED.getCode());

        return applicationInfoRepo.updateById(applicationInfo);
    }

    @Override
    public Boolean rejectApplication(Long projectId, String userWallet, String projectAddress) {
        List<ApplicationInfo> applicationInfos = applicationInfoRepo.queryListByProjectId(projectId);
        ApplicationInfo applicationInfo = applicationInfos.stream()
                .filter(x -> ApplicationStatusEnum.APPLIED.getCode().equals(x.getApplicationStatus()) && x.getReviewerAddress().equals(userWallet))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(applicationInfo)){
            return false;
        }
        applicationInfo.setApplicationStatus(ApplicationStatusEnum.REJECTED.getCode());

        return applicationInfoRepo.updateById(applicationInfo);
    }
}
