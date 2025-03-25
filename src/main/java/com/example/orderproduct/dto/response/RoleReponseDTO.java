package com.example.orderproduct.dto.response;

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
public class RoleReponseDTO {
    private Long id;
    private String name;
    private String description;
    private Long status;
    List<ModuleReponseDTO> moduleReponseDTO;
}
