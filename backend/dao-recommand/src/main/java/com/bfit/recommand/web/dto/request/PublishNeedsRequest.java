package com.bfit.recommand.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublishNeedsRequest {
    /**
     *     title: '',
     *     type: '',
     *     description: '',
     *     budget: '',
     *     currency: '',
     *     startTime: '',
     *     endTime: ''
     */
    @NotBlank
    private String userWallet;
    private String description;
    @NotBlank
    private String title;
    @NotNull
    @Min(0)
    private BigDecimal reward;
    @NotBlank
    private String crypto;
    private Date startTime;
    private Date endTime;
    private String type;



}
