package com.simit.security.sms.controller;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.simit.security.sms.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/sms")
public class SmsController {

    private SmsService smsService;

    @Autowired
    public SmsController(SmsService smsService){
        this.smsService = smsService;
    }

    @GetMapping("/code/{phoneNumber}")
    public ResponseEntity<String> sendCode(@PathVariable("phoneNumber") String phoneNumber, HttpSession session){
        SendSmsResponse sendSmsResponse = null;
        String code = getRandomCode();
        try {
//            sendSmsResponse = smsService.sendSms(phoneNumber, code);
        } catch (Exception e) {
            e.printStackTrace();
        }


        if(/*sendSmsResponse.getCode().equals("OK")*/ true){ // 验证码发送成功，保存到 Session

            Map<String, String> smsCode = new HashMap<>();
            smsCode.put("code", "123456");
            smsCode.put("createAt", LocalDateTime.now().toString());

            session.setAttribute(phoneNumber, smsCode);
            return new ResponseEntity<>("验证码发送成功！", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("验证码发送失败，请稍后再试！", HttpStatus.OK);
        }
    }

    private String getRandomCode() {
        return (int) ((Math.random() * 9 + 1) * 100000) + "";
    }

}
