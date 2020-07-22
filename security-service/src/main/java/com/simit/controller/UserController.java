package com.simit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.simit.entity.Result;
import com.simit.entity.User;
import com.simit.service.UserService;
import com.simit.utils.JwtUtil;
import com.simit.utils.ResultUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/regist")
    public Result<Object> signup(@RequestBody User user, HttpSession session){

        Map<String, String> smsCode = (Map<String, String>) session.getAttribute(user.getPhoneNumber());
        String code = smsCode.get("code");
        LocalDateTime createAt = LocalDateTime.parse(smsCode.get("createAt"));

        if(!code.equals(user.getSmsCode())){
            return ResultUtil.error("验证码不正确");
        }

        if(createAt.plusMinutes(5).isBefore(LocalDateTime.now())){
            return ResultUtil.error("验证码失效");
        }

        // 检查用户是否已经存在
        Map<String, Object> username = new HashMap<>();
        username.put("username", user.getUsername());
        Collection<User> users = userService.listByMap(username);
        if(users.size() != 0){
            return ResultUtil.error("用户名已存在");
        }


        user.setLastTime(LocalDateTime.now().format(formatter));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userService.save(user);

        session.removeAttribute("smsCode");
        return ResultUtil.data(null);
    }


    @GetMapping("/info")
    public Result<User> getUserInfo(@RequestParam String access_token){

        if(!JwtUtil.isSigned(access_token) || !JwtUtil.verify(access_token)){
            return null;
        }

        Claims claims = JwtUtil.getClaim(access_token);        // 暂时写到这里，等认证那边处理好用户再继续

        String username = (String) claims.get("user_name");

        QueryWrapper<User> queryByUsername = new QueryWrapper<>();
        queryByUsername.lambda().eq(User::getUsername, username);
        List<User> users = userService.list(queryByUsername);

        User user = users.get(0);
        user.setPassword(null);
        return new ResultUtil<User>().setData(user);
    }
}
