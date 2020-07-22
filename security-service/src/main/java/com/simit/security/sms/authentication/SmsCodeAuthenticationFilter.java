package com.simit.security.sms.authentication;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SmsCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    /**
     * form表单中手机号码的字段name
     */
    public static final String SPRING_SECURITY_FORM_PHONE_NUMBER_KEY = "phoneNumber";

    private String phoneNumberParameter = "phoneNumber";

    /**
     * 是否仅 POST 方式
     */
    private boolean postOnly = true;

    public SmsCodeAuthenticationFilter() {
        // 短信登录的请求 post 方式的 /user/sms/login
        super(new AntPathRequestMatcher("/sms/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        if (postOnly && !httpServletRequest.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + httpServletRequest.getMethod());
        }

        String phoneNumber = obtainPhoneNumber(httpServletRequest);

        if (phoneNumber == null) {
            phoneNumber = "";
        }

        phoneNumber = phoneNumber.trim();

        SmsCodeAuthenticationToken authRequest = new SmsCodeAuthenticationToken(phoneNumber);

        // Allow subclasses to set the "details" property
        setDetails(httpServletRequest, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }


    protected String obtainPhoneNumber(HttpServletRequest request) {
        return request.getParameter(phoneNumberParameter);
    }

    protected void setDetails(HttpServletRequest request, SmsCodeAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    public String getPhoneNumberParameter() {
        return phoneNumberParameter;
    }

    public void setPhoneNumberParameter(String phoneNumberParameter) {
        Assert.hasText(phoneNumberParameter, "Mobile parameter must not be empty or null");
        this.phoneNumberParameter = phoneNumberParameter;
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }
}
