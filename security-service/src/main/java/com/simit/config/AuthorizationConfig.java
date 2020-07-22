package com.simit.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    public RedisConnectionFactory redisConnectionFactory;       // redis工厂，默认使用lettue

    @Autowired
    private AuthenticationManager authenticationManager;        // 用户认证管理器

    @Qualifier("userRepositoryUserDetailsService")
    @Autowired
    private UserDetailsService userDetailsService;              // 用户服务

    @Autowired
    PasswordEncoder passwordEncoder;                            // 密码加密器

    @Autowired
    DataSource dataSource;

    /**
     * 客户端信息配置，可配置多个客户端，这里可以使用配置文件进行代替
     *
     * @param clients 客户端设置
     * @throws Exception 异常
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.inMemory()
//                .withClient("client-a")                                                              // client端唯一标识
//                .secret(passwordEncoder.encode("123456"))                                      //  client-a的密码，这里的密码应该是加密后的
//                .authorizedGrantTypes("password", "refresh_token")                                           // 授权模式标识，这里主要测试用password模式，另外refresh_token不是一种模式，但是可以使用它来刷新access_token（在它的有效期内）
//                .scopes("all");

        clients.jdbc(dataSource);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .tokenStore(tokenStore())                                   // 设置jwtToken为tokenStore
                .accessTokenConverter(jwtAccessTokenConverter());              // 设置access_token转换器
    }

    /**
     * 授权服务安全配置，主要用于放行客户端访问授权服务接口
     *
     * @param security AuthorizationServerSecurityConfigurer
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.allowFormAuthenticationForClients()                          // 允许表单提交
                .checkTokenAccess("isAuthenticated()")
                .tokenKeyAccess("permitAll()");
    }

    /**
     * jwt访问token转换器
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("my-sign-key");                                // 资源服务器需要配置此选项方能解密jwt的token
        return converter;
    }


    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

//    @Bean
//    public TokenStore tokenStore() {
//        //使用redis存储token
//        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
//        //设置redis token存储中的前缀
//        redisTokenStore.setPrefix("auth-token:");
//        return redisTokenStore;
//    }

//    @Bean
//    public DefaultTokenServices tokenService() {
//        DefaultTokenServices tokenServices = new DefaultTokenServices();
//        //配置token存储
//        tokenServices.setTokenStore(tokenStore());
//        //开启支持refresh_token，此处如果之前没有配置，启动服务后再配置重启服务，可能会导致不返回token的问题，解决方式：清除redis对应token存储
//        tokenServices.setSupportRefreshToken(true);
//        //复用refresh_token
//        tokenServices.setReuseRefreshToken(true);
//        //token有效期，设置12小时
//        tokenServices.setAccessTokenValiditySeconds(12 * 60 * 60);
//        //refresh_token有效期，设置一周
//        tokenServices.setRefreshTokenValiditySeconds(7 * 24 * 60 * 60);
//        return tokenServices;
//    }

//    /**
//     * jwt的token存储对象
//     */
//    @Bean
//    public JwtTokenStore jwtTokenStore(){
//        return new JwtTokenStore(jwtAccessTokenConverter());
//    }
}
