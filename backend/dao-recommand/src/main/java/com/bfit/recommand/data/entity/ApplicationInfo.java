package com.bfit.recommand.data.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ApplicationInfo {

    private Long id;
    private String reviewerAddress;
    private String projectAddress;
    private Long projectId;
    private Integer applicationStatus;
    @JsonIgnore
    private Date dbCreateTime;
    @JsonIgnore
    private Date dbUpdateTime;
    @TableLogic(value = "0")
    private Boolean deleted;

}
