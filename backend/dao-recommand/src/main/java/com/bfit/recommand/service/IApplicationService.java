package com.bfit.recommand.service;

public interface IApplicationService {

    Boolean joinApplication(Long projectId, String userWallet, String projectAddress);

    Boolean acceptApplication(Long projectId, String userWallet, String projectAddress);

    Boolean rejectApplication(Long projectId, String userWallet, String projectAddress);
}
