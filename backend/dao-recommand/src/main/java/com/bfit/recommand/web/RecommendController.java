package com.bfit.recommand.web;

import com.bfit.recommand.common.dto.CommonResult;
import com.bfit.recommand.service.RecommendService;
import com.bfit.recommand.web.dto.RecommandNeed;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@RequiredArgsConstructor
@RestController("recommend")
@RequestMapping("/recommend")
public class RecommendController {

    private final RecommendService recommendService;

    @CrossOrigin
    @GetMapping("/list")
    public CommonResult<List<RecommandNeed>> fetchRecommandNeeds(@RequestParam @Validated @NotBlank String walletAddress,
                                                                 @RequestParam @Validated @NotBlank String item){
        return CommonResult.ok(recommendService.queryRecommend(walletAddress,item));
    }

}
