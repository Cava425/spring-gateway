package com.simit.controller;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.simit.data.UserRepository;
import com.simit.entity.User;
import com.simit.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SmsService smsService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return new ResponseEntity<String>("200", HttpStatus.OK);
    }

    @GetMapping("/code/{phoneNumber}")
    public ResponseEntity<String> sendCode(@PathVariable("phoneNumber") String phoneNumber){
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = smsService.sendSms(phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(sendSmsResponse.getCode().equals("OK")&&sendSmsResponse.getMessage().equals("OK")){
            return new ResponseEntity<>("验证码发送成功！", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("验证码发送失败，请稍后再试！", HttpStatus.OK);
        }
    }
}
