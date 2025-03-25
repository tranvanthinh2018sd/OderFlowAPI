package com.example.orderproduct.exception;

import com.example.orderproduct.constrant.AppConst;
import com.example.orderproduct.dto.response.BaseReponseDTO;
import com.example.orderproduct.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.Locale;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionHandler {

    private final MessageUtils messageUtils;

    @ExceptionHandler({BadRequestException.class})
    @ResponseStatus(HttpStatus.OK)
    public BaseReponseDTO<Object> handlerBadRequestException(BadRequestException badRequestException) {
        return BaseReponseDTO.<Object>builder()
                .code(400)
                .message(badRequestException.getMessage())
                .isSuccess(false)
                .build();
    }
    @ExceptionHandler({SQLException.class})
    @ResponseStatus(HttpStatus.OK)
    public BaseReponseDTO<Object> handlerOracleDatabaseException(SQLException dataIntegrityViolationException) {
        return BaseReponseDTO.<Object>builder()
                .code(400)
                .message(dataIntegrityViolationException.getMessage())
                .isSuccess(false)
                .build();
    }
    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.OK)
    public BaseReponseDTO<Object> handlerConstraintViolationException(ConstraintViolationException constraintViolationException) {
        return BaseReponseDTO.<Object>builder()
                .code(400)
                .message("Data is valid")
                .isSuccess(false)
                .build();
    }
    @ExceptionHandler({ResourceNotFoundException.class})
    @ResponseStatus(HttpStatus.OK)
    public BaseReponseDTO<Object> handlerResourceNotFoundException(ResourceNotFoundException resourceNotFoundException) {
        return BaseReponseDTO.<Object>builder()
                .code(400)
                .message("Data not found")
                .isSuccess(false)
                .build();
    }
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.OK)
    public BaseReponseDTO<Object> handleIOException (UnauthorizedException exception) {
        Locale locale = new Locale("en");
        return BaseReponseDTO.builder()
                .message(messageUtils.getMessageSource(exception.getMessage(), locale))
                .isSuccess(Boolean.FALSE)
                .code(AppConst.STATUS_FAIL)
                .build();
    }
}
