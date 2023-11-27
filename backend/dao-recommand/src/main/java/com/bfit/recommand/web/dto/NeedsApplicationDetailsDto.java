package com.bfit.recommand.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NeedsApplicationDetailsDto {

    private String applicationUserName;
    private String applicationUserAvatar;
    private String applicationId;
    private Integer applicationStatus;
    private Date createTime;
    private List<PersonalNeedsDto.MessageDto> messageDtoList;

}
