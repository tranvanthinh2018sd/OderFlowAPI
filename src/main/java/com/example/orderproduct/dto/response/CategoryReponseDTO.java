package com.example.orderproduct.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryReponseDTO {
    private Long id;
    private String name;
    private String description;
    private Long status;
}
