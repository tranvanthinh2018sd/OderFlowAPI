package com.example.orderproduct.controller;


import com.example.orderproduct.dto.request.LoginRequestDTO;
import com.example.orderproduct.dto.response.BaseReponseDTO;
import com.example.orderproduct.dto.response.LoginReponseDTO;
import com.example.orderproduct.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public BaseReponseDTO<LoginReponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse httpServletResponse, Locale locale){
        return authService.login(loginRequestDTO, httpServletResponse, locale);
    }
}
