package com.example.orderproduct.service;

import com.example.orderproduct.dto.request.LoginRequestDTO;
import com.example.orderproduct.dto.response.BaseReponseDTO;
import com.example.orderproduct.dto.response.LoginReponseDTO;
import com.example.orderproduct.dto.response.UserReponseDTO;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Locale;

public interface AuthService {
    BaseReponseDTO<LoginReponseDTO> login(LoginRequestDTO loginRequestDTO, HttpServletResponse httpServletResponse, Locale locale);
    Long getUserIdFromToken(HttpServletRequest request);
    BaseReponseDTO<UserReponseDTO> getInformation(HttpServletRequest request, Locale locale);
    BaseReponseDTO<String> logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale);
    BaseReponseDTO<Object> sentOtpResetPassword(String email, Locale locale) throws MessagingException;
}
