package com.example.orderproduct.dto.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {
    private Long id;
    private String name;
    private Double price;
    private String image;
    private String description;
    private Long status;
    List<Long> categoryIds;
    List<Long> tagIds;
    public ProductRequestDTO(String name, String image, String description, Long status, List<Long> categoryIds, List<Long> tagIds) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.status = status;
        this.categoryIds = categoryIds;
        this.tagIds = tagIds;
    }
}
