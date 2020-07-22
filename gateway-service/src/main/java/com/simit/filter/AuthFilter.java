package com.simit.filter;

import com.simit.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 全局过滤器，对每一个请求都会过滤，检查是否符合规则，此项目暂时没有用到
 */
public class AuthFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpResponse response = exchange.getResponse();

        // 跳过不需要验证的路径
        String url = exchange.getRequest().getURI().getPath();
        if(url.startsWith("/oauth/token")){
            return chain.filter(exchange);
        }
        if(url.startsWith("/user")){
            return chain.filter(exchange);
        }

        // 从请求头中取出token
        String token = exchange.getRequest().getHeaders().getFirst("Authorization").substring(7);
        if(token == null || token.isEmpty()){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
            byte[] entity = "{\"code\": \"401\",\"message\": \"401 Unauthorized.\"}".getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = response.bufferFactory().wrap(entity);
            return response.writeWith(Flux.just(buffer));
        }

        if(!JwtUtil.isSigned(token) || !JwtUtil.verify(token)){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
            byte[] entity = "{\"code\": \"10001\",\"message\": \"invalid token.\"}".getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = response.bufferFactory().wrap(entity);
            return response.writeWith(Flux.just(buffer));
        }

        Claims claims = JwtUtil.getClaim(token);        // 暂时写到这里，等认证那边处理好用户再继续

        return chain.filter(exchange);
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
