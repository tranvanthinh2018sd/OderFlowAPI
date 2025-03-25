package com.example.orderproduct.service;

import com.example.orderproduct.dto.request.RoleRequestDTO;
import com.example.orderproduct.dto.response.BaseReponseDTO;
import com.example.orderproduct.dto.response.RoleReponseDTO;
import com.example.orderproduct.entity.RoleEntity;

import java.util.List;
import java.util.Locale;

public interface RoleService {
    BaseReponseDTO<List<RoleEntity>> getAllRoles(String search,int page, int size,Locale locale);
    BaseReponseDTO<RoleReponseDTO> getRoleById(long id, Locale locale);
    BaseReponseDTO<RoleReponseDTO> createRole(RoleRequestDTO requestDTO, Locale locale);
    BaseReponseDTO<RoleReponseDTO> updateRole( RoleRequestDTO requestDTO, Locale locale);
}
