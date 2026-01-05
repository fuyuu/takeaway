package com.hope;

import com.hope.domain.pojo.User;
import com.hope.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Jwt {
    @Test
    void send() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("123456");
        user.setEmail("admin@gmail.hope");
        String token = JwtUtil.createToken(user.getUsername());
        System.out.println("token  --"+token);

        Claims claims = JwtUtil.parseToken(token);
        System.out.println("claims --"+claims);
    }
    @Test
    void getUser() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("123456");
//        user.setEmail("admin@gmail.hope");
//        String token = "eyJ0eXAiOiJqd3AiLCJhbGciOiJIUzI1NiJ9.eyJjcmVhdGVUb2tlbiI6ImNyZWF0ZVRva2VuIiwiaWQiOiIxIiwianRpIjoiYjc5YTRhMGYtNjk0OC00NDU3LWExNTktZDk4NDkzYzdhY2QyIiwic3ViIjoiMSIsImlhdCI6MTc1ODYyNzc4MCwiZXhwIjoxNzU4NjI5NTgwfQ.8mK_zr0U57jwlTDeukAii07pK8D4FKTy-qz6Veommgc";
//        System.out.println("token  --"+token);
//
//        Claims claims = JwtUtil.parseToken(token);
//        System.out.println("claims --"+claims.getSubject());
//        System.out.println(claims.getId());
        System.out.println(user);
    }
}
