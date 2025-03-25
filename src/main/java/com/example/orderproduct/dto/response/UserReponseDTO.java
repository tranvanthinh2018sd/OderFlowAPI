package com.example.orderproduct.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserReponseDTO {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String image;
    private String address;
    private Date creaDate;
    private Date updateDate;
    private Long status;
    List<RoleReponseDTO> roles;
    public UserReponseDTO(Long id, String username, String email, String phone, String image, String address, Long status) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.image = image;
        this.address = address;
        this.status = status;
    }
}
