package com.bfit.recommand.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bfit.recommand.data.entity.ApplicationInfo;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface ApplicationInfoMapper extends BaseMapper<ApplicationInfo> {
}
