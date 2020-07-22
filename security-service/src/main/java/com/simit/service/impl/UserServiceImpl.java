package com.simit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simit.data.mapper.UserMapper;
import com.simit.entity.User;
import com.simit.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
