package com.example.orderproduct.service;

import com.example.orderproduct.dto.request.UserRequestDTO;
import com.example.orderproduct.dto.response.BaseReponseDTO;
import com.example.orderproduct.dto.response.UserReponseDTO;
import com.example.orderproduct.entity.UserEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public interface UserService {
    BaseReponseDTO<List<UserReponseDTO>> getAllUserPaging(String search,int page, int size, Locale locale);
    BaseReponseDTO<UserReponseDTO> getUserById(Long id, Locale locale);
    BaseReponseDTO<UserReponseDTO> createUser(UserRequestDTO request, MultipartFile imageRequest, Locale locale) throws IOException;
    BaseReponseDTO<UserReponseDTO> updateUser(UserRequestDTO request, MultipartFile fileImage, Locale locale);
    BaseReponseDTO <String> confirmUser(Long userId, String verifyCode, Locale locale);
    BaseReponseDTO<Object> savePassswordForOTP(Long passwordResetTokenId, String token, String password, Locale locale);
}
