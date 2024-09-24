package com.nuc.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuc.sys.pojo.Message;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
}
