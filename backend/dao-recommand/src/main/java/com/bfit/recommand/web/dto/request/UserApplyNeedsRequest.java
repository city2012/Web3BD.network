package com.bfit.recommand.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserApplyNeedsRequest {
    @NotNull
    private String walletAddress;
    @NotNull
    private Long projectId;
    private String projectAddress;
    @NotNull
    private Integer applyStatus;

}
