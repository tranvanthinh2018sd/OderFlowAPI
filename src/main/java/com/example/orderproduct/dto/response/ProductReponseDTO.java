package com.example.orderproduct.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductReponseDTO {
    private Long id;
    private String name;
    private Double price;
    private String image;
    private String description;
    private Long status;
    List<CategoryReponseDTO> categories;
    List<TagReponseDTO> tags;
}
