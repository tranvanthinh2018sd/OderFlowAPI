package com.example.orderproduct.mapper;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {
    public Cookie createCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60); // 1 hour
        return cookie;
    }
}
