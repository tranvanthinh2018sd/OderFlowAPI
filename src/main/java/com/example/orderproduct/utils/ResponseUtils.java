package com.example.orderproduct.utils;

import com.example.orderproduct.dto.response.BaseReponseDTO;
import com.example.orderproduct.dto.response.PaginationReponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;


import java.util.Locale;


public class ResponseUtils {

    public static <T> BaseReponseDTO<T> buildResponse(int code, String messageKey, Locale locale, PaginationReponseDTO paginationReponseDTO, T data, MessageSource messageSource) {
        return BaseReponseDTO.<T>builder()
                .code(code)
                .isSuccess(code == 0)
                .message(messageSource.getMessage(messageKey, null, locale))
                .pagination(paginationReponseDTO)
                .data(data)
                .build();
    }
}
