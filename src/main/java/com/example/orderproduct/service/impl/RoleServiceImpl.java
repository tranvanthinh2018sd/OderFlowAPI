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
import com.example.orderproduct.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final RoleModuleReponsitory roleModuleReponsitory;
    private final MessageSource messageSource;

    @Override
    public BaseReponseDTO<List<RoleEntity>> getAllRoles(String search,int page, int size, Locale locale) {
        try{
            int offset = page * size;
            int totalElements = roleRepository.countPagin(search);
            int totalPages = (int) Math.ceil((double) totalElements / size);
            List<RoleEntity> roleEntity= roleRepository.findAll(search,offset, size);
            PaginationReponseDTO paginationReponseDTO = PaginationReponseDTO.builder()
                    .totalItems(totalElements)
                    .totalPages(totalPages)
                    .currentPage(page)
                    .size(size)
                    .build();
            if(roleEntity.isEmpty()){
                return ResponseUtils.buildResponse(-1, MessageConst.LIST_ROLE_NOT_FOUND, locale, null,null, messageSource);
            }
            return ResponseUtils.buildResponse(0, MessageConst.GET_DATA_SUCCESS, locale, paginationReponseDTO,null, messageSource);

        } catch (Exception e) {
            return BaseReponseDTO.<List<RoleEntity>>builder()
                    .code(-2)
                    .isSuccess(true)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @Override
    public BaseReponseDTO<RoleReponseDTO> getRoleById(long id, Locale locale) {
        try{
            RoleEntity roleEntity = roleRepository.findById(id).orElse(null);
            if(roleEntity == null){
                return ResponseUtils.buildResponse(-1, MessageConst.ROLE_NOT_FOUND, locale, null,null, messageSource);
            }
            List<Long> moduleId = roleModuleReponsitory.findByRoleId(roleEntity.getId()).stream().map(RoleModuleEntity::getModuleId).collect(Collectors.toList());
            RoleReponseDTO reponseDTO = roleMapper.toRoleReponseDTO(roleEntity, moduleId);

            return ResponseUtils.buildResponse(0, MessageConst.GET_DATA_SUCCESS, locale, null,reponseDTO, messageSource);

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
           return  ResponseUtils.buildResponse(0, MessageConst.GET_DATA_SUCCESS, locale, null,reponseDTO, messageSource);
       }
       catch (Exception e){
           return BaseReponseDTO.<RoleReponseDTO>builder()
                   .code(-1)
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
                return ResponseUtils.buildResponse(-1, MessageConst.ROLE_NOT_FOUND, locale, null,null, messageSource);
            }
            return ResponseUtils.buildResponse(0, MessageConst.GET_DATA_SUCCESS, locale, null,reponseDTO, messageSource);
        } catch (Exception e) {
            log.error("#Error"+e.getMessage());
            return BaseReponseDTO.<RoleReponseDTO>builder()
                    .code(-2)
                    .isSuccess(true)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }
}
