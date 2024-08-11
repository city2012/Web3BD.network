package com.bfit.recommand.web;

import com.bfit.recommand.common.dto.CommonResult;
import com.bfit.recommand.service.impl.NeedsServiceImpl;
import com.bfit.recommand.web.dto.HomeNeedsDto;
import com.bfit.recommand.web.dto.NeedsApplicationDetailsDto;
import com.bfit.recommand.web.dto.PersonalNeedsDto;
import com.bfit.recommand.web.dto.request.PublishNeedsRequest;
import com.bfit.recommand.web.dto.request.UserApplyNeedsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@RestController("needs")
@RequestMapping("/api")
public class NeedsController {

    /**
     * /api/public_needs
     * /api/my_needs
     * /api/related_needs
     * /api/join_needs
     * /api/accept_needs
     * /api/reject_needs
     * /api/stop_needs
     * /api/complete_needs
     * /api/confirm_needs
     */
//    @Qualifier("needsServiceImpl")
    private final NeedsServiceImpl needsService;


    @CrossOrigin
    @GetMapping("/public_needs")
    public CommonResult<List<HomeNeedsDto>> getPublicNeeds(){
        return CommonResult.ok(needsService.getPublicNeeds());
    }

    @CrossOrigin
    @GetMapping("/my_needs")
    public CommonResult<List<PersonalNeedsDto>> getUserNeeds(@RequestParam String userWallet,
                                                             @RequestParam Integer relationType){
        return CommonResult.ok(needsService.getUserNeeds(userWallet, relationType));
    }

    @CrossOrigin
    @GetMapping("/my_needs/application/list")
    public CommonResult<List<NeedsApplicationDetailsDto>> getUserNeedsAppList(@RequestParam String projectId){
        return CommonResult.ok(needsService.getNeedsAppList(projectId));
    }

    @CrossOrigin
    @PostMapping("/publish_needs")
    public CommonResult<Boolean> publishNeeds(@RequestBody @Validated PublishNeedsRequest request){

        if (Objects.isNull(request)){
            return CommonResult.ok(false);
        }

        return CommonResult.ok(needsService.publishNeeds(request));
    }

    @CrossOrigin
    @GetMapping("/related_needs")
    public CommonResult<List<Object>> getRelatedNeeds(){
        return CommonResult.ok();
    }

    @CrossOrigin
    @PostMapping("/join_needs")
    public CommonResult<Boolean> joinNeeds(@RequestBody @Validated UserApplyNeedsRequest request){
        return CommonResult.ok(needsService.joinNeeds(request));
    }

    @CrossOrigin
    @PostMapping("/accept_needs")
    public CommonResult<Boolean> acceptNeeds(@RequestBody @Validated UserApplyNeedsRequest request){
        return CommonResult.ok(needsService.acceptNeeds(request));
    }

    @CrossOrigin
    @PostMapping("/reject_needs")
    public CommonResult<Boolean> rejectNeeds(@RequestBody @Validated UserApplyNeedsRequest request){
        return CommonResult.ok(needsService.rejectNeeds(request));
    }

//    @CrossOrigin
//    @PostMapping("/stop_needs")
//    public CommonResult<Boolean> stopNeeds(){
//        return CommonResult.ok(true);
//    }

    @CrossOrigin
    @PostMapping("/complete_needs")
    public CommonResult<Boolean> completeNeeds(@RequestBody @Validated UserApplyNeedsRequest request){
        return CommonResult.ok(needsService.completeNeeds(request));
    }

    @CrossOrigin
    @PostMapping("/confirm_needs")
    public CommonResult<Boolean> finalConfirmNeeds(@RequestBody @Validated UserApplyNeedsRequest request){
        return CommonResult.ok(needsService.finalConfirmNeeds(request));
    }

}
