package com.simit.utils;

import io.jsonwebtoken.*;

public class JwtUtil {
    private static final String key = "my-sign-key";

    /**
     * 校验是不是jwt签名
     * @param token
     * @return
     */
    public static boolean isSigned(String token){
        return  Jwts.parser()
                .setSigningKey(key)
                .isSigned(token);
    }

    /**
     * 校验签名是否正确
     * @param token
     * @return
     */
    public static boolean verify(String token){
        try {
            Jwts.parser()
                    .setSigningKey(key.getBytes())            // 这里使用 key.getBytes()
                    .parseClaimsJws(token);
            return true;
        }catch (JwtException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取payload 部分内容（即要传的信息）
     * 使用方法：如获取 userId：getClaim(token).get("userId");
     * @param token
     * @return
     */
    public static Claims getClaim(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(key.getBytes())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claims;
    }
}
