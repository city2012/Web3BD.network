package com.bfit.recommand.data.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_info")
public class UserInfo {

    @TableId
    private Long id;
    private Long organizationId;
    private Integer level;
    private String userName;
    private String userWallet;
    private String avatar;
    private String userEmail;
    private String socialLinks;
    private String description;
    private String other;
    private Date dbCreateTime;
    private Date dbUpdateTime;
    @TableLogic(value = "0")
    private boolean deleted;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SocialLinkDto{
        private String name;
        private String link;
    }

}
