package com.example.orderproduct.service;

import com.example.orderproduct.dto.response.BaseReponseDTO;
import jakarta.mail.MessagingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;

public interface MailService {
    BaseReponseDTO<String> sendSimpleMail(String to, String subject, String content, MultipartFile[] files) throws MessagingException;
    void sendComfirmLink(String email, Long id, String sercretCode) throws MessagingException, UnsupportedEncodingException;
}
