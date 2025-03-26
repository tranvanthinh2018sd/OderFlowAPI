package com.example.orderproduct.controller;


import com.example.orderproduct.dto.request.UserRequestDTO;
import com.example.orderproduct.dto.response.BaseReponseDTO;
import com.example.orderproduct.dto.response.UserReponseDTO;
import com.example.orderproduct.entity.UserEntity;
import com.example.orderproduct.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    public final UserService userService;

    @GetMapping("/all")
    public BaseReponseDTO<List<UserReponseDTO>> getAllUsers(String search, int page, int size, Locale locale) {
        BaseReponseDTO<List<UserReponseDTO>> reponseDTO = userService.getAllUserPaging(search,page, size, locale);
        return reponseDTO;
    }

    @GetMapping("/by-id")
    public BaseReponseDTO<UserReponseDTO> getAllById(Long id, Locale locale) {
        BaseReponseDTO<UserReponseDTO> reponseDTO = userService.getUserById(id, locale);
        return reponseDTO;
    }
    @PostMapping("/create")
    public BaseReponseDTO<UserReponseDTO> createUser( UserRequestDTO requestDTO, @RequestParam(value = "fileImage", required = false) MultipartFile fileImage, Locale locale) throws IOException {
        BaseReponseDTO<UserReponseDTO> reponseDTO = userService.createUser(requestDTO, fileImage, locale);
        return reponseDTO;
    }
    @PostMapping("/update")
    public BaseReponseDTO<UserReponseDTO> updateUser(UserRequestDTO requestDTO, @RequestParam(value = "fileImage", required = false) MultipartFile fileImage, Locale locale) {
        BaseReponseDTO<UserReponseDTO> reponseDTO = userService.updateUser(requestDTO, locale);
        return reponseDTO;
    }
    @GetMapping("/confirm/{userId}")
    public BaseReponseDTO<String> confirm(@PathVariable Long userId, @RequestParam String verifyCode) {
            return userService.confirmUser(userId, verifyCode);

    }
}
