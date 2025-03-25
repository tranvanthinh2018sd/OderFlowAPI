package com.example.orderproduct.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginReponseDTO {
    private Long id;
    private String username;
    private String token;
    private RoleReponseDTO role;
}
