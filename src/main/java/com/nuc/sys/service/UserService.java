package com.nuc.sys.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nuc.sys.pojo.User;
import com.nuc.sys.pojo.UserDTO;

import java.util.List;

public interface UserService extends IService<User> {
    Page<UserDTO> getUserDtoPage(Page<User> pageUser);

    UserDTO getDtoById(int id);

    List<UserDTO> getUserDtoList(List<User> list);

    boolean SaveImportUser(List<UserDTO> users);
}
