package com.simit.config;

import com.simit.security.UserRepositoryUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception{
        return super.authenticationManager();
    }

    // 这里自己定义了UserRepositoryUserDetailsService
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return super.userDetailsService();
//    }

    @Autowired
    UserRepositoryUserDetailsService userDetailsService;

    /**
     * 用户认证
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("alice")
//                .password(passwordEncoder().encode("123456"))
//                .authorities(Collections.emptyList())
//                .and()
//                .withUser("bob")
//                .password(passwordEncoder().encode("123456"))
//                .roles("USER");
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

//    /**
//     * 用户鉴权
//     *
//     * @param http
//     * @throws Exception
//     */
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.apply(smsCodeAuthenticationSecurityConfig).
//                and()
//                .authorizeRequests()
//                .antMatchers("/user/**", "/h2-console/**").permitAll()
//                .antMatchers(HttpMethod.OPTIONS).permitAll()                  //放行options方法请求
//                .anyRequest()
//                .authenticated()                 // 所有请求都需要通过认证
//                .and()
//                .httpBasic()                     // Basic提交
//                .and()
//                .csrf().disable();               // 关跨域保护
//
//        http.headers().frameOptions().disable();
//    }
}

