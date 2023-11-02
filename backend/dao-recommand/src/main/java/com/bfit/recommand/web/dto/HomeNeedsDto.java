package com.bfit.recommand.web.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class HomeNeedsDto {

    private BigDecimal reward;
    private String crypto;
    private String avatar;
    private String organizationName;
    private String needsName;
    private String walletAddress;
    private String description;
    private String projectTag;
    private Date createTime;
    private Date lastModifyTime;
    private String projectStatus;

}