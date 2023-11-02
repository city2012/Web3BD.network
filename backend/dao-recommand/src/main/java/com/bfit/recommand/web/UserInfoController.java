package com.bfit.recommand.web;

import com.bfit.recommand.common.dto.CommonResult;
import com.bfit.recommand.service.UserInfoService;
import com.bfit.recommand.web.dto.UserEmailInfoDto;
import com.bfit.recommand.web.dto.UserInfoDto;
import com.bfit.recommand.web.dto.request.UserRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController("user")
@RequestMapping(path = {"/user","/api"})
public class UserInfoController {

    private final UserInfoService userInfoService;

    @CrossOrigin
    @PostMapping("/email/registration")
    public CommonResult<Boolean> fetchRecommandNeeds(@RequestBody @Validated UserEmailInfoDto userEmailInfoDto){
        return CommonResult.ok(userInfoService.registerEmail(userEmailInfoDto.getEmail(), userEmailInfoDto.getWalletAddress()));
    }

    @CrossOrigin
    @GetMapping("/organization")
    public CommonResult<UserInfoDto> fetchOrganizationInfo(@RequestParam("addr") String walletAddress){
        return CommonResult.ok(userInfoService.fetchUserInfo(walletAddress));
    }

    @CrossOrigin
    @GetMapping("/person")
    public CommonResult<UserInfoDto> fetchPersonInfo(@RequestParam("addr") String walletAddress){
        return CommonResult.ok(userInfoService.fetchUserInfo(walletAddress));
    }

    @CrossOrigin
    @PostMapping("/registration")
    public CommonResult<Boolean> register(@RequestBody @Validated UserRegisterRequest request){
        return CommonResult.ok(userInfoService.register(request));
    }

    @CrossOrigin
    @GetMapping("/organization/list")
    public CommonResult<List<UserInfoDto>> fetchOrganizationInfoList(@RequestParam(name = "exceptAddr", required = false) String walletAddress){
        return CommonResult.ok(userInfoService.fetchOrganizationList(walletAddress));
    }

}
