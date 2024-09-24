package com.nuc.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuc.sys.pojo.College;
import com.nuc.sys.pojo.Department;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CollegeMapper extends BaseMapper<College> {
}
