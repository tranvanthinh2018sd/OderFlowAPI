package com.example.orderproduct.service;

import com.example.orderproduct.dto.response.BaseReponseDTO;
import jakarta.mail.MessagingException;
import org.springframework.cglib.core.Local;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

public interface MailService {
    BaseReponseDTO<String> sendSimpleMail(String to, String subject, String content, MultipartFile[] files, Locale locale) throws MessagingException;
    void sendComfirmLink(String email, Long id, String sercretCode) throws MessagingException, UnsupportedEncodingException;
    String sendSimpleMail(String to, String subject, String content) throws MessagingException;
}
