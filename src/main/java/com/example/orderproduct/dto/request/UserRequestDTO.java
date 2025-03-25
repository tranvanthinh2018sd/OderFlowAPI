package com.example.orderproduct.dto.request;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class UserRequestDTO{
    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String image;
    private String address;
    private Date creaDate;
    private Date updateDate;
    private Long status;
    List<Long> roleId;
    public UserRequestDTO(String username, String password, String email, String phone, String image, String address, Date creaDate, Date updateDate, Long status, List<Long> roleId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.image = image;
        this.address = address;
        this.creaDate = creaDate;
        this.updateDate = updateDate;
        this.status = status;
        this.roleId = roleId;
    }
}
