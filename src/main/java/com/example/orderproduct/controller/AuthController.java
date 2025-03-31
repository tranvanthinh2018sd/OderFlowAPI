package com.example.orderproduct.controller;


import com.example.orderproduct.dto.request.LoginRequestDTO;
import com.example.orderproduct.dto.request.ResetPasswordRequestDTO;
import com.example.orderproduct.dto.response.BaseReponseDTO;
import com.example.orderproduct.dto.response.LoginReponseDTO;
import com.example.orderproduct.dto.response.UserReponseDTO;
import com.example.orderproduct.service.AuthService;
import com.example.orderproduct.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public BaseReponseDTO<LoginReponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse httpServletResponse, Locale locale){
        return authService.login(loginRequestDTO, httpServletResponse, locale);
    }
    @PostMapping("/user-info")
    public BaseReponseDTO<UserReponseDTO> userInfo(HttpServletRequest request, Locale locale){
        return authService.getInformation(request, locale);
    }
    @GetMapping("/logout")
    public BaseReponseDTO<String> logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {
        return authService.logout(httpServletRequest, httpServletResponse, locale);
    }

    @GetMapping("/test-api")
    public BaseReponseDTO<String> testApi() {
        return BaseReponseDTO.<String>builder()
                .code(0)
                .isSuccess(true)
                .message("Hello World")
                .data(null)
                .build();
    }

    @PostMapping("/fogot-password")
    public BaseReponseDTO<Object> fogotPasswordWithOTP(String email, Locale locale) throws MessagingException {
        return authService.sentOtpResetPassword(email, locale);
    }

    @PostMapping("/save-password-otp")
    public BaseReponseDTO<Object> savePassWordWithOTP(@RequestBody ResetPasswordRequestDTO requestDTO, Locale locale) throws MessagingException {
        return userService.savePassswordForOTP(requestDTO.getPasswordResetTokenId(), requestDTO.getOtp(), requestDTO.getPassword(), locale);
    }

}
