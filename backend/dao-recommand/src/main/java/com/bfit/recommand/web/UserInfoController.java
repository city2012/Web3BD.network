package com.bfit.recommand.web;

import com.bfit.recommand.common.dto.CommonResult;
import com.bfit.recommand.service.UserInfoService;
import com.bfit.recommand.web.dto.UserEmailInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController("user")
@RequestMapping("/user")
public class UserInfoController {

    private final UserInfoService userInfoService;

    @PostMapping("/email/registration")
    public CommonResult<Boolean> fetchRecommandNeeds(@RequestBody @Validated UserEmailInfoDto userEmailInfoDto){
        return CommonResult.ok(userInfoService.registerEmail(userEmailInfoDto.getEmail(), userEmailInfoDto.getWalletAddress()));
    }


}
