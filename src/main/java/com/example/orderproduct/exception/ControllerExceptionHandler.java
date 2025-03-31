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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseReponseDTO<Object> handlerBadRequestException(BadRequestException badRequestException) {
        return BaseReponseDTO.<Object>builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .isSuccess(Boolean.FALSE)
                .message(badRequestException.getMessage())
                .build();
    }
    @ExceptionHandler({SQLException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseReponseDTO<Object> handlerOracleDatabaseException(SQLException dataIntegrityViolationException) {
        return BaseReponseDTO.<Object>builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .isSuccess(Boolean.FALSE)
                .message(dataIntegrityViolationException.getMessage())
                .build();
    }
    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseReponseDTO<Object> handlerConstraintViolationException(ConstraintViolationException constraintViolationException) {
        return BaseReponseDTO.<Object>builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .isSuccess(Boolean.FALSE)
                .message(constraintViolationException.getMessage())
                .build();
    }
    @ExceptionHandler({ResourceNotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseReponseDTO<Object> handlerResourceNotFoundException(ResourceNotFoundException resourceNotFoundException) {
        return BaseReponseDTO.<Object>builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .isSuccess(Boolean.FALSE)
                .message(resourceNotFoundException.getMessage())
                .build();
    }
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseReponseDTO<Object> handleIOException (UnauthorizedException exception) {
        return BaseReponseDTO.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .isSuccess(Boolean.FALSE)
                .message(exception.getMessage())
                .build();
    }
}
