package com.bfit.recommand.data.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class InstanceMessage {

    @TableId
    private Long id;
    private String reviewerAddress;
    private String projectAddress;
    private String message;
    private Date dbCreateTime;
    private Date dbUpdateTime;
    @TableLogic(delval = "0")
    private Boolean deleted;

}
