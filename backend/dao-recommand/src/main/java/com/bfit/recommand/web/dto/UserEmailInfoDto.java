package com.bfit.recommand.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEmailInfoDto {
    @NotBlank
    private String email;
    private String walletAddress;
}
