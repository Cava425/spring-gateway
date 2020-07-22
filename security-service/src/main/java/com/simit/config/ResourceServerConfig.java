package com.simit.config;

import com.simit.security.sms.authentication.SmsCodeAuthenticationSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
//    @Autowired
//    JwtAccessTokenConverter jwtAccessTokenConverter;
//    @Autowired
//    JwtTokenStore jwtTokenStore;

    @Autowired
    TokenStore tokenStore;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;






    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // 无状态
        resources.stateless(true);
        // 设置token存储
        resources.tokenStore(tokenStore);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.apply(smsCodeAuthenticationSecurityConfig)
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/**", "/sms/**", "/user/**", "/h2-console/**").permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll()                  //放行options方法请求
                .anyRequest()
                .authenticated()                 // 所有请求都需要通过认证
                .and()
                .httpBasic()                     // Basic提交
                .and()
                .csrf().disable();               // 关跨域保护
        http.headers().frameOptions().disable();
    }

    //    /**
//     * 设置token存储，这一点配置要与授权服务器相一致
//     */
//    @Bean
//    public RedisTokenStore tokenStore(){
//        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
//        redisTokenStore.setPrefix("auth-token:");
//        return redisTokenStore;
//    }









//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);   // 设置创建session策略
//        http.authorizeRequests().anyRequest().authenticated();                               // 所有请求必须授权
//    }
//
//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) {
//        resources.tokenStore(jwtTokenStore);
//    }

//    /**
//     * jwt访问token转换器
//     */
//    @Bean
//    public JwtAccessTokenConverter jwtAccessTokenConverter(){
//        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//        converter.setSigningKey("my-sign-key");                                               // 与授权服务器相同的signingKey
//        return converter;
//    }

//    /**
//     * jwt的token存储对象
//     */
//    @Bean
//    public JwtTokenStore jwtTokenStore(){
//        return new JwtTokenStore(jwtAccessTokenConverter());
//    }

}

