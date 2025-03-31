package com.example.orderproduct.service.impl;

import com.example.orderproduct.constrant.MessageConst;
import com.example.orderproduct.dto.response.BaseReponseDTO;
import com.example.orderproduct.service.MailService;
import com.example.orderproduct.utils.ResponseUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final MessageSource messageSource;

    @Value("${endpoint.confirmUser}")
    private String apiConfirmUser;

    @Value("${spring.mail.from}")
    private String emailFrom;

    @Override
    public BaseReponseDTO<String> sendSimpleMail(String to, String subject, String content, MultipartFile[] files, Locale locale) throws MessagingException {
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");
            helper.setFrom(emailFrom);

            if(to.contains(",")){
                helper.setTo(InternetAddress.parse(to));
            }
            else {
                helper.setTo(to);
            }
            if(files != null){
                for(MultipartFile file : files){
                    helper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()),file);
                }
            }
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
            return ResponseUtils.buildResponse(0, MessageConst.MAIL_SEND_SUCCESS, locale, null,null, messageSource);


        }catch (Exception e){
            log.info("#Error: "+e.getMessage());
            return ResponseUtils.buildResponse(-1, MessageConst.MAIL_SEND_FAIL, locale, null,null, messageSource);
        }
    }

    @Override
    public String sendSimpleMail(String to, String subject, String content) throws MessagingException {
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");
            helper.setFrom(emailFrom);

            if(to.contains(",")){
                helper.setTo(InternetAddress.parse(to));
            }
            else {
                helper.setTo(to);
            }
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
            return "";
        }catch (Exception e){
            return e.getMessage();
        }
    }

    @Override
    public void sendComfirmLink(String email, Long userId, String sercretCode) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

        Context context = new Context();

        String linkConfirm = String.format("%s/%s?verifyCode=%s", apiConfirmUser, userId, sercretCode);
        Map<String, Object> properties = new HashMap<>();
        properties.put("linkConfirm", linkConfirm);
        context.setVariables(properties);
        helper.setFrom(emailFrom, "Thinh Dev");
        helper.setTo(email);
        helper.setSubject("Please confirm your account");

        String html = templateEngine.process("confirm-email.html", context);
        helper.setText(html, true);
        mailSender.send(message);
    }
}
