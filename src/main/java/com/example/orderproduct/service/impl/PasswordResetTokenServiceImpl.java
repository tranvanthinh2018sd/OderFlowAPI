package com.example.orderproduct.service.impl;

import com.example.orderproduct.entity.PasswordResetTokenEntity;
import com.example.orderproduct.repository.PasswordResetTokenRepository;
import com.example.orderproduct.service.PasswordResetTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public Long createPasswordResetTokenForUser(Long userId, String token) {
        PasswordResetTokenEntity passwordResetToken = new PasswordResetTokenEntity();
        passwordResetToken.setUserId(userId);
        passwordResetToken.setToken(token);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        passwordResetToken.setExpiryDate(calendar.getTime());
        PasswordResetTokenEntity saved = passwordResetTokenRepository.save(passwordResetToken);
        return saved.getId();
    }
}
