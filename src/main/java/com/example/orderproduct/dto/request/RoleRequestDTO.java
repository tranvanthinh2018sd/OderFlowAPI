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
public class RoleRequestDTO {
    private Long id;
    private String name;
    private String description;
    private Long status;
    private List<Long> moduleId;
    public RoleRequestDTO(String name, String description, Long status, List<Long> moduleId) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.moduleId = moduleId;
    }
}
