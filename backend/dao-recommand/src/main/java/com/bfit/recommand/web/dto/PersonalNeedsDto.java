package com.bfit.recommand.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageDto{
        private String name;
        private String message;
        private Date createTime;
    }

}
