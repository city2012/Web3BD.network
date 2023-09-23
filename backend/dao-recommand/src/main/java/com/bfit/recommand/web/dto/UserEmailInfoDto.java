package com.bfit.recommand.web.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserEmailInfoDto {
    @NotBlank
    private String email;
    private String walletAddress;
}
