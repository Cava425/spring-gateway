package com.simit.security.wechat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@RestController
@RequestMapping("/oauth/wechat")
public class CallbackController {

    private String appid = "wxa825fd97d8d0502a";
    private String appSecret = "a0ddae7dca0f12bb789700f443124950";
    private String redirectUri = "http://wechattoken.ranchip.com/oauth/wechat/callBack";

    private RestTemplate rest;

    @Autowired
    public CallbackController(RestTemplate rest){
        this.rest = rest;
    }


    @GetMapping("/login")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = "https://open.weixin.qq.com/connect/qrconnect?" +
                "appid=" + appid +
                "&redirect_uri=" + URLEncoder.encode(redirectUri, "UTF-8") +
                "&response_type=code&scope=snsapi_login" +
                "&state=" + request.getSession().getId() +
                "#wechat_redirect";

        response.sendRedirect(url);
    }

    @GetMapping("/callBack")
    public void callBackLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // 先取出微信返回的 code，ps:目前仅支持授权码模式
        String code = request.getParameter("code");
        String state = request.getParameter("state");

        // 检查 code & state

        // 使用 code 获取 access_token
        String getAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=" + appid +
                "&secret=" + appSecret +
                "&code=" + code +
                "&grant_type=authorization_code";
        ResponseEntity<String> accessTokenResponse = rest.getForEntity(getAccessTokenUrl, String.class);

        // 检查请求是否成功，获取响应体内容
        JSONObject accessTokenResponseBody = JSON.parseObject(accessTokenResponse.getBody());

        // 取出 access_token openid
        String access_token = accessTokenResponseBody.getString("access_token");
        String openid = accessTokenResponseBody.getString("openid");

        // 使用 access_token openid 获取用户的信息
        String getUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token +
                "&openid=" + openid;
        ResponseEntity<String> userInfoResponse = rest.getForEntity(getUserInfoUrl, String.class);
        // 检查请求是否成功，获取响应体内容
        JSONObject userInfoResponseBody = JSON.parseObject(accessTokenResponse.getBody());

        // 使用获取的用户信息注册到数据库中


        // 向认证授权服务器发送请求，获取JWT
        String getJWTUrl = "";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, String> para = new LinkedMultiValueMap<>();
        para.add("username", "");
        para.add("password", "");

        HttpEntity requestEntity = new HttpEntity(para, headers);
        ResponseEntity<String> JWTResponse = rest.postForEntity("", requestEntity, String.class);

        // 得到 JWTToken 以后，重定向到网络应用
        String redirectUrl = "url" + "?access_token=xxx";
        response.sendRedirect(redirectUrl);

        System.out.println(userInfoResponseBody);

    }


}
