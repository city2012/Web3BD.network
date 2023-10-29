package com.bfit.recommand.web.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserInfoDto {

    private String userName;
    private String userWallet;
    private String avatar;
    private String userEmail;
    private List<SocilLinks> socialLinkList;
    private Integer noticeNum;

    @Data
    @Builder
    public static class SocilLinks{

        private String name;
        private String link;

    }

}
