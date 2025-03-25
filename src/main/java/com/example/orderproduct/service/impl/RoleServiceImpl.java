package com.example.orderproduct.service.impl;

import com.example.orderproduct.constrant.MessageConst;
import com.example.orderproduct.dto.request.RoleRequestDTO;
import com.example.orderproduct.dto.response.BaseReponseDTO;
import com.example.orderproduct.dto.response.PaginationReponseDTO;
import com.example.orderproduct.dto.response.RoleReponseDTO;
import com.example.orderproduct.entity.RoleEntity;
import com.example.orderproduct.entity.RoleModuleEntity;
import com.example.orderproduct.mapper.RoleMapper;
import com.example.orderproduct.repository.RoleModuleReponsitory;
import com.example.orderproduct.repository.RoleRepository;
import com.example.orderproduct.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleModuleReponsitory roleModuleReponsitory;
    @Autowired
    private MessageSource messageSource;

    @Override
    public BaseReponseDTO<List<RoleEntity>> getAllRoles(String search,int page, int size, Locale locale) {
        try{
            int offset = page * size;
            // Đếm tổng số phần tử
            int totalElements = roleRepository.countPagin(search);
            // Tổng số page
            int totalPages = (int) Math.ceil((double) totalElements / size);
            List<RoleEntity> roleEntity= roleRepository.findAll(search,offset, size);
            PaginationReponseDTO paginationReponseDTO = PaginationReponseDTO.builder()
                    .totalItems(totalElements)
                    .totalPages(totalPages)
                    .currentPage(page)
                    .size(size)
                    .build();
            if(roleEntity.isEmpty()){
                return BaseReponseDTO.<List<RoleEntity>>builder()
                        .code(-1)
                        .isSuccess(false)
                        .message(messageSource.getMessage(MessageConst.LIST_ROLE_NOT_FOUND,null, locale))
                        .pagination(null)
                        .data(roleEntity)
                        .build();
            }
            return BaseReponseDTO.<List<RoleEntity>>builder()
                    .code(0)
                    .isSuccess(true)
                    .message(messageSource.getMessage(MessageConst.GET_DATA_SUCCESS,null, locale))
                    .pagination(paginationReponseDTO)
                    .data(roleEntity)
                    .build();
        } catch (Exception e) {
            return BaseReponseDTO.<List<RoleEntity>>builder()
                    .code(-2)
                    .isSuccess(true)
                    .message(e.getMessage())
                    .pagination(null)
                    .data(null)
                    .build();
        }
    }

    @Override
    public BaseReponseDTO<RoleReponseDTO> getRoleById(long id, Locale locale) {
        try{
            RoleEntity roleEntity = roleRepository.findById(id).orElse(null);
            if(roleEntity == null){
                return BaseReponseDTO.<RoleReponseDTO>builder()
                        .code(-1)
                        .isSuccess(true)
                        .message(messageSource.getMessage(MessageConst.ROLE_NOT_FOUND,null, locale))
                        .data(null)
                        .build();
            }
            List<Long> moduleId = roleModuleReponsitory.findByRoleId(roleEntity.getId()).stream().map(RoleModuleEntity::getModuleId).collect(Collectors.toList());
            RoleReponseDTO reponseDTO = roleMapper.toRoleReponseDTO(roleEntity, moduleId);

            return BaseReponseDTO.<RoleReponseDTO>builder()
                    .code(0)
                    .isSuccess(true)
                    .message(messageSource.getMessage(MessageConst.GET_DATA_SUCCESS,null, locale))
                    .data(reponseDTO)
                    .build();
        } catch (Exception e) {
            return BaseReponseDTO.<RoleReponseDTO>builder()
                    .code(-2)
                    .isSuccess(false)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @Override
    public BaseReponseDTO<RoleReponseDTO> createRole(RoleRequestDTO requestDTO, Locale locale){
       try{
           RoleEntity roleEntity = roleMapper.createRole(requestDTO,requestDTO.getModuleId());
           RoleReponseDTO reponseDTO = roleMapper.toRoleReponseDTO(roleEntity,requestDTO.getModuleId());
           return BaseReponseDTO.<RoleReponseDTO>builder()
                   .code(0)
                   .isSuccess(true)
                   .message(messageSource.getMessage(MessageConst.GET_DATA_SUCCESS,null, locale))
                   .data(reponseDTO)
                   .build();
       }
       catch (Exception e){
           return BaseReponseDTO.<RoleReponseDTO>builder()
                   .code(-2)
                   .isSuccess(false)
                   .message(e.getMessage())
                   .data(null)
                   .build();
       }
    }
    @Override
    public BaseReponseDTO<RoleReponseDTO> updateRole( RoleRequestDTO requestDTO, Locale locale){
        try{
            RoleReponseDTO reponseDTO = roleMapper.updateRole(requestDTO);
            if(reponseDTO == null){
                return BaseReponseDTO.<RoleReponseDTO>builder()
                        .code(-1)
                        .isSuccess(true)
                        .message(messageSource.getMessage(MessageConst.ROLE_NOT_FOUND,null, locale))
                        .data(null)
                        .build();
            }
            return BaseReponseDTO.<RoleReponseDTO>builder()
                    .code(0)
                    .isSuccess(true)
                    .message(messageSource.getMessage(MessageConst.GET_DATA_SUCCESS,null, locale))
                    .data(reponseDTO)
                    .build();
        } catch (Exception e) {
            return BaseReponseDTO.<RoleReponseDTO>builder()
                    .code(-2)
                    .isSuccess(true)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }
}
