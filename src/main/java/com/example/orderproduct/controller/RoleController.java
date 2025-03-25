package com.example.orderproduct.controller;

import com.example.orderproduct.dto.request.RoleRequestDTO;
import com.example.orderproduct.dto.response.BaseReponseDTO;
import com.example.orderproduct.dto.response.RoleReponseDTO;
import com.example.orderproduct.entity.RoleEntity;
import com.example.orderproduct.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping("/all")
    public BaseReponseDTO<List<RoleEntity>> getAllRoles(String search,int page, int size, Locale locale) {
        BaseReponseDTO<List<RoleEntity>> reponseDTOS = roleService.getAllRoles(search,page,size,locale);
        return reponseDTOS;
    }
    @GetMapping("/by-id")
    public BaseReponseDTO<RoleReponseDTO> getRoleById(int id, Locale locale) {
        BaseReponseDTO<RoleReponseDTO> roleReponseDTO = roleService.getRoleById(id,locale);
        return roleReponseDTO;
    }
    @PostMapping("/create")
    public BaseReponseDTO<RoleReponseDTO> createRole(@RequestBody RoleRequestDTO requestDTO, Locale locale) {
        BaseReponseDTO<RoleReponseDTO> reponseDTO = roleService.createRole(requestDTO,locale);
        return reponseDTO;
    }
    @PostMapping("/update")
    public BaseReponseDTO<RoleReponseDTO> updateRole(@RequestBody RoleRequestDTO requestDTO, Locale locale) {
        BaseReponseDTO<RoleReponseDTO> roleReponseDTO = roleService.updateRole(requestDTO,locale);
        return roleReponseDTO;
    }
}
