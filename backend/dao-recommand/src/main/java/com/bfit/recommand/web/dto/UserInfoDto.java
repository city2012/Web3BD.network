package com.bfit.recommand.web.dto;

import com.bfit.recommand.data.entity.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {

    private String userName;
    private String userWallet;
    private String avatar;
    private String userEmail;
    private List<UserInfo.SocialLinkDto> socialLinkList;
    private Integer noticeNum;

}
