package com.bfit.recommand.web.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class PersonalNeedsDto {

    private BigDecimal reward;
    private String crypto;
    private String avatar;
    private String organizationName;
    private String needsName;
    private String walletAddress;
    private String description;
    private String projectTag;
    private List<MessageDto> messageList;
    private Date createTime;
    private Date lastModifyTime;
    private String projectStatus;

    @Data
    @Builder
    public static class MessageDto{
        private String name;
        private String message;
        private Date createTime;
    }

}
