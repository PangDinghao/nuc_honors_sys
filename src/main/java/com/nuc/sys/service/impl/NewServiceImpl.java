package com.nuc.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuc.sys.mapper.NewMapper;
import com.nuc.sys.pojo.New;
import com.nuc.sys.service.NewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewServiceImpl extends ServiceImpl<NewMapper, New> implements NewService {
    @Autowired
    private NewMapper newMapper;

    @Override
    public List<New> listhot() {
        LambdaQueryWrapper<New> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(New::getType, 0);


        return newMapper.selectList(wrapper);
    }
}
