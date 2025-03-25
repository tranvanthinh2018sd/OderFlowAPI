package com.example.orderproduct.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageUtils {
    @Autowired
    private MessageSource messageSource;

    public String getMessageSource(String key) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, null, locale);
    }

    public String getMessageSource(String key, Locale locale) {
        return messageSource.getMessage(key, null, locale);
    }

    public String getMessageSource(String key, Object[] arg, Locale locale) {
        return messageSource.getMessage(key, arg, locale);
    }

    public String getMessageSource(String key, Object[] arg) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, arg, locale);
    }
}
