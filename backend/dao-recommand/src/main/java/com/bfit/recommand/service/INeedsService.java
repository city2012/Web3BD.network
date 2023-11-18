package com.bfit.recommand.service;

import com.bfit.recommand.web.dto.HomeNeedsDto;
import com.bfit.recommand.web.dto.NeedsApplicationDetailsDto;
import com.bfit.recommand.web.dto.PersonalNeedsDto;
import com.bfit.recommand.web.dto.request.PublishNeedsRequest;
import com.bfit.recommand.web.dto.request.UserApplyNeedsRequest;

import java.util.List;

public interface INeedsService {

    List<HomeNeedsDto> getPublicNeeds();

    List<PersonalNeedsDto> getUserNeeds(String userWallet, Integer relationType);

    Boolean publishNeeds(PublishNeedsRequest request);

    List<NeedsApplicationDetailsDto> getNeedsAppList(String projectId);

    Boolean joinNeeds(UserApplyNeedsRequest request);

    Boolean acceptNeeds(UserApplyNeedsRequest request);

    Boolean rejectNeeds(UserApplyNeedsRequest request);

    Boolean completeNeeds(UserApplyNeedsRequest request);

    Boolean finalConfirmNeeds(UserApplyNeedsRequest request);
}
