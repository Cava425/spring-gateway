package com.simit;

import com.simit.data.mapper.UserMapper;
import com.simit.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SecurityServiceApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void getAllUsers(){
        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }

}
