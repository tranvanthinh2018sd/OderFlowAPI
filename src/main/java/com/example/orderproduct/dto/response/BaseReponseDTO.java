package com.example.orderproduct.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseReponseDTO <T>{
    private int code;
    private Boolean isSuccess;
    private String message;
    private PaginationReponseDTO pagination;
    private T data;

}
