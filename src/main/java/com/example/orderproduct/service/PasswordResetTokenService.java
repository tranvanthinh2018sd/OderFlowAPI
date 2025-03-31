package com.example.orderproduct.service;

public interface PasswordResetTokenService {
    Long createPasswordResetTokenForUser(Long userId, String token);
}
