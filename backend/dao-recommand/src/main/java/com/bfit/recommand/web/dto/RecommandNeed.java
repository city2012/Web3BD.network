package com.bfit.recommand.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommandNeed {

    private String avatar;
    private String name;
    private String walletAddress;

}
