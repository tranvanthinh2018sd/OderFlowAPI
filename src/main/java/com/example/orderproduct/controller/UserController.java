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

    @GetMapping()
    public BaseReponseDTO<List<UserReponseDTO>> getAllUsers(String search, int page, int size, Locale locale) {
        log.info("#Search: {}, Page: {}, Size: {}", search, page, size);
        BaseReponseDTO<List<UserReponseDTO>> reponseDTO = userService.getAllUserPaging(search,page, size, locale);
        log.info("#Response: {}", reponseDTO);
        return reponseDTO;
    }

    @GetMapping("/{id}")
    public BaseReponseDTO<UserReponseDTO> getAllById(Long id, Locale locale) {
        log.info("#Id: {}", id);
        BaseReponseDTO<UserReponseDTO> reponse = userService.getUserById(id, locale);
        log.info("#Reponse: {}", reponse);
        return reponse;
    }
    @PostMapping("/create")
    public BaseReponseDTO<UserReponseDTO> createUser(
            @RequestBody UserRequestDTO requestDTO,
            @RequestParam(value = "fileImage", required = false) MultipartFile fileImage,
            Locale locale
    ) throws IOException {
        log.info("#Request create: {}", requestDTO);
        BaseReponseDTO<UserReponseDTO> reponseDTO =  userService.createUser(requestDTO, fileImage, locale);
        log.info("#Response update: {}", reponseDTO);
         return reponseDTO;
    }
    @PostMapping("/update")
    public BaseReponseDTO<UserReponseDTO> updateUser(
            @RequestBody UserRequestDTO requestDTO,
            @RequestParam(value = "fileImage", required = false) MultipartFile fileImage,
            Locale locale
    ) {
        log.info("#Request update: {}", requestDTO);
        BaseReponseDTO<UserReponseDTO> reponseDTO = userService.updateUser(requestDTO, fileImage, locale);
        log.info("#Response update: {}", reponseDTO);
        return reponseDTO;
    }
    @GetMapping("/confirm/{userId}")
    public BaseReponseDTO<String> confirm(@PathVariable Long userId, @RequestParam String verifyCode, Locale locale) {
            return userService.confirmUser(userId, verifyCode, locale);

    }
}
