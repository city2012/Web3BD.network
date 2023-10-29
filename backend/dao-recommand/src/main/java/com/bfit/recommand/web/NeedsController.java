package com.bfit.recommand.web;

import com.bfit.recommand.common.dto.CommonResult;
import com.bfit.recommand.service.NeedsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private final NeedsService needsService;


    @CrossOrigin
    @GetMapping("/public_needs")
    public CommonResult<List<Object>> getPublicNeeds(){
        return CommonResult.ok(needsService.getPublicNeeds());
    }

    @CrossOrigin
    @GetMapping("/my_needs")
    public CommonResult<List<Object>> getUserNeeds(@RequestParam String userWallet){
        return CommonResult.ok(needsService.getUserNeeds(userWallet));
    }

    @CrossOrigin
    @GetMapping("/related_needs")
    public CommonResult<List<Object>> getRelatedNeeds(){
        return CommonResult.ok();
    }

    @CrossOrigin
    @PostMapping("/join_needs")
    public CommonResult<Boolean> joinNeeds(){
        return CommonResult.ok(true);
    }

    @CrossOrigin
    @PostMapping("/accept_needs")
    public CommonResult<Boolean> acceptNeeds(){
        return CommonResult.ok(true);
    }

    @CrossOrigin
    @PostMapping("/reject_needs")
    public CommonResult<Boolean> rejectNeeds(){
        return CommonResult.ok(true);
    }

    @CrossOrigin
    @PostMapping("/stop_needs")
    public CommonResult<Boolean> stopNeeds(){
        return CommonResult.ok(true);
    }

    @CrossOrigin
    @PostMapping("/complete_needs")
    public CommonResult<Boolean> completeNeeds(){
        return CommonResult.ok(true);
    }

    @CrossOrigin
    @PostMapping("/confirm_needs")
    public CommonResult<Boolean> finalConfirmNeeds(){
        return CommonResult.ok(true);
    }

}
