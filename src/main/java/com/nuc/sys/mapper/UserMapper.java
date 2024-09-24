package com.nuc.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuc.sys.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
