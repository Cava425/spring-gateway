package com.simit.security.sms.authentication;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;

        String phoneNumber = (String) authenticationToken.getPrincipal();

        checkSmsCode(phoneNumber);

        UserDetails userDetails = userDetailsService.loadUserByUsername(phoneNumber);

        // 此时鉴权成功后，应当重新 new 一个拥有鉴权的 authenticationResult 返回
        SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(userDetails, userDetails.getAuthorities());

        authenticationResult.setDetails(authenticationToken.getDetails());

        return authenticationResult;
    }

    private void checkSmsCode(String phoneNumber) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String inputCode = request.getParameter("smsCode");

        Map<String, String> smsCode = (Map<String, String>) request.getSession().getAttribute("smsCode");
        if(smsCode == null) {
            throw new BadCredentialsException("发送验证码失败，请稍后再试");
        }

        String applyPhoneNumber = smsCode.get("phoneNumber");
        int code = Integer.parseInt(smsCode.get("code"));

        if(!applyPhoneNumber.equals(phoneNumber)) {
            throw new BadCredentialsException("手机号和验证码不匹配，请确认手机号后再试");
        }
        if(code != Integer.parseInt(inputCode)) {
            throw new BadCredentialsException("验证码错误，请稍后再试");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 判断 authentication 是不是 SmsCodeAuthenticationToken 的子类或子接口
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
