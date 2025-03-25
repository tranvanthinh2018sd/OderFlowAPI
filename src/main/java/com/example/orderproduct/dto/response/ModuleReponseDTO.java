package com.example.orderproduct.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class ModuleReponseDTO {
    private Long id;
    private String title;
    private String link;
    private String description;
    private String icon;
    private Date createDate;
    private Long staus;
}
