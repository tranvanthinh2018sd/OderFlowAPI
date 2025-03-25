package com.example.orderproduct.config.security.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "config.jwt.secret")
public class JwtInfoConfig {
    private long accessTokenExpiredTime;
    private long refreshTokenExpiredTime;
    private String privateKey;
    private String publicKey;
}
