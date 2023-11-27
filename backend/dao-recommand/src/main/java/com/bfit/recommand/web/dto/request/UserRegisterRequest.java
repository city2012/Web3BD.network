package com.bfit.recommand.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {

    /**
     *     logo: null,
     *     productName: '',
     *     productDescription: '',
     *     website: '',
     *     twitter: '',
     *     other: '',
     *     checkbox: false,
     */
    private String avatar;
    private String userName;
    private String description;
    private String website;
    private String twitter;
    private String userEmail;
    private String other;
    private Boolean checkbox;
    private Boolean personFlag;
    private String userWallet;
    private String orgId;


}
