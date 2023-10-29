package com.bfit.recommand.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bfit.recommand.data.entity.InstanceMessage;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface InstanceMessageMapper extends BaseMapper<InstanceMessage> {
}
