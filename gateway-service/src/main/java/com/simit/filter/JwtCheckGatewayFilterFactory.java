package com.simit.filter;

import com.simit.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class JwtCheckGatewayFilterFactory extends AbstractGatewayFilterFactory {


    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {

            ServerHttpResponse response = exchange.getResponse();

            // 从请求头中取出token
            String authorization = exchange.getRequest().getHeaders().getFirst("Authorization");
            if(authorization == null || authorization.isEmpty()){
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
                byte[] entity = "{\"code\": \"401\",\"message\": \"401 Unauthorized.\"}".getBytes(StandardCharsets.UTF_8);
                DataBuffer buffer = response.bufferFactory().wrap(entity);
                return response.writeWith(Flux.just(buffer));
            }

            String token = authorization.substring(7);
            if(!JwtUtil.isSigned(token) || !JwtUtil.verify(token)){
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
                byte[] entity = "{\"code\": \"10001\",\"message\": \"invalid token.\"}".getBytes(StandardCharsets.UTF_8);
                DataBuffer buffer = response.bufferFactory().wrap(entity);
                return response.writeWith(Flux.just(buffer));
            }

            Claims claims = JwtUtil.getClaim(token);        // 暂时写到这里，等认证那边处理好用户再继续

            return chain.filter(exchange);
        };
    }
}
