package com.nuc.sys;

import com.nuc.sys.mapper.UserMapper;
import com.nuc.sys.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NucHonorsSysApplicationTests {

    @Autowired
    UserMapper userMapper;

    //usermapper测试
    @Test
    void contextLoads() {
        User user = new User();
        user.setName("Zs");
        userMapper.insert(user);
    }

}
