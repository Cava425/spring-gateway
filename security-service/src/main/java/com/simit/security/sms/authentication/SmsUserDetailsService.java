package com.simit.security.sms.authentication;

import com.google.common.collect.Lists;
import com.simit.data.jpa.UserRepository;
import com.simit.entity.Role;
import com.simit.entity.User;
import com.simit.security.SecurityUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class SmsUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public SmsUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        // 从数据库中取出用户信息
        User user = userRepository.findByPhoneNumber(username);

        // 判断用户是否存在
        if (user == null) {
            throw new UsernameNotFoundException("手机号不存在");
        }

        // 添加权限
//        List<Role> userRoles = userRoleService.listByUserId(user.getId());
//        for (SysUserRole userRole : userRoles) {
//            SysRole role = roleService.selectById(userRole.getRoleId());
//            authorities.add(new SimpleGrantedAuthority(role.getName()));
//        }
//
//        List<Role> roles = Lists.newArrayList(new Role(1L, "USER"));
//        user.setRoles(roles);

        // 返回UserDetails实现类
        return new SecurityUserDetails(user);
    }
}
