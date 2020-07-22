package com.simit.security.sms.service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.simit.data.jpa.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class SmsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    //产品名称:云通信短信API产品,开发者无需替换
    @Value("${spring.ali.sms.product}")
    private String product;
    //产品域名,开发者无需替换
    @Value("${spring.ali.sms.domain}")
    private String domain;
    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    @Value("${spring.ali.sms.accessKeyId}")
    private String accessKeyId;
    @Value("${spring.ali.sms.accessKeySecret}")
    private String accessKeySecret;

    public SendSmsResponse sendSms(String phoneNumber, String code) throws com.aliyuncs.exceptions.ClientException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(phoneNumber);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName("ranchip");
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode("SMS_187575781");
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam("{ \"code\":" + code + "}");
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        return sendSmsResponse;
    }
}
