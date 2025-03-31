package com.example.orderproduct.controller;

import com.example.orderproduct.dto.response.BaseReponseDTO;
import com.example.orderproduct.service.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/common")
public class CommonController {
    private final MailService mailService;

    @PostMapping("/send-email")
    public BaseReponseDTO<String> sendEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String content, @RequestParam(required = false) MultipartFile[] files, Locale locale) throws MessagingException {
        return mailService.sendSimpleMail(to,subject,content,files, locale);
    }
}
