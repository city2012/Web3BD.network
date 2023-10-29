package com.bfit.recommand.web.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class HomeNeedsDto {

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
