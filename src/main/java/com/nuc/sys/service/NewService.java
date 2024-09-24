package com.nuc.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.nuc.sys.pojo.New;
import com.nuc.sys.pojo.User;

import java.util.List;

public interface NewService extends IService<New> {
    List<New> listhot();
}
