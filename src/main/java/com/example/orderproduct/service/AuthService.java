package com.example.orderproduct.service;

import com.example.orderproduct.dto.request.LoginRequestDTO;
import com.example.orderproduct.dto.response.BaseReponseDTO;
import com.example.orderproduct.dto.response.LoginReponseDTO;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Locale;

public interface AuthService {
    BaseReponseDTO<LoginReponseDTO> login(LoginRequestDTO loginRequestDTO, HttpServletResponse httpServletResponse, Locale locale);
}
