package com.simit.security;

import com.simit.data.jdbc.UserRolesRepository;
import com.simit.data.jpa.RoleRepository;
import com.simit.data.jpa.UserRepository;
import com.simit.entity.Role;
import com.simit.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class UserRepositoryUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserRolesRepository userRolesRepository;

    @Autowired
    public UserRepositoryUserDetailsService(UserRepository userRepository, RoleRepository roleRepository, UserRolesRepository userRolesRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRolesRepository = userRolesRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String args) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(args);
        if(user != null){
//            Iterable<UserRole> userRoles = userRolesRepository.findByUserId(user.getId());
            List<Role> roles = new ArrayList<>();
            roles.add(new Role("USER"));
//            userRoles.forEach((ur -> {
//                roles.add(roleRepository.findById(ur.getRolesId()).get());
//            }));
//
            user.setRoles(roles);
            return new SecurityUserDetails(user);
        }

        // 判断用户是否是用邮箱或者手机号登录
        if(isEmail(args)){
            user = userRepository.findByEmail(args);
        }else if(isPhoneNumber(args)){
            user = userRepository.findByPhoneNumber(args);
        }

        if(user != null){
            return new SecurityUserDetails(user);
        }

//        Iterable<UserRole> userRoles = userRolesRepository.findByUserId(user.getId());
//        List<Role> roles = new ArrayList<>();
//        userRoles.forEach((ur -> {
//            roles.add(roleRepository.findById(ur.getRolesId()).get());
//        }));
//
//        user.setRoles(roles);

        throw new UsernameNotFoundException("用户不存在！");
    }


    private boolean isEmail(String email){
        boolean flag = false;
        try{
            String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        }catch(Exception e){
            flag = false;
        }

        return flag;
    }

    public static boolean isPhoneNumber(String mobiles){
        boolean flag = false;
        try{
            Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
            Matcher m = p.matcher(mobiles);
            flag = m.matches();
        }catch(Exception e){
            flag = false;
        }
        return flag;
    }
}
