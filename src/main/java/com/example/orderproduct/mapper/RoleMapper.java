package com.example.orderproduct.mapper;

import com.example.orderproduct.constrant.MessageConst;
import com.example.orderproduct.dto.request.RoleRequestDTO;
import com.example.orderproduct.dto.response.ModuleReponseDTO;
import com.example.orderproduct.dto.response.RoleReponseDTO;
import com.example.orderproduct.entity.RoleEntity;
import com.example.orderproduct.entity.RoleModuleEntity;
import com.example.orderproduct.exception.ResourceNotFoundException;
import com.example.orderproduct.repository.ModuleRepository;
import com.example.orderproduct.repository.RoleModuleReponsitory;
import com.example.orderproduct.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleMapper {

    private final ModuleRepository moduleRepository;
    private final RoleRepository roleRepository;
    private final RoleModuleReponsitory roleModuleReponsitory;
    public RoleReponseDTO toRoleReponseDTO(RoleEntity role, List<Long> moduleId) {
        RoleReponseDTO roleReponseDTO = new RoleReponseDTO();
        roleReponseDTO.setId(role.getId());
        roleReponseDTO.setName(role.getName());
        roleReponseDTO.setDescription(role.getDescription());
        roleReponseDTO.setStatus(role.getStatus());
        List<ModuleReponseDTO> modules = new ArrayList<>();
        for(Long module : moduleId) {
            moduleRepository.findById(module).ifPresent(moduleReponse -> {
                ModuleReponseDTO moduleReponseDTO = new ModuleReponseDTO();
                moduleReponseDTO.setId(moduleReponse.getId());
                moduleReponseDTO.setTitle(moduleReponse.getTitle());
                moduleReponseDTO.setIcon(moduleReponse.getIcon());
                moduleReponseDTO.setLink(moduleReponse.getLink());
                moduleReponseDTO.setStaus(moduleReponse.getStaus());
                moduleReponseDTO.setDescription(moduleReponse.getDescription());
                modules.add(moduleReponseDTO);
            });
        }
        roleReponseDTO.setModuleReponseDTO(modules);
        return roleReponseDTO;
    }

    public RoleEntity createRole(RoleRequestDTO requestDTO, List<Long> moduleId) {
        RoleEntity role = new RoleEntity();
        role.setName(requestDTO.getName());
        role.setDescription(requestDTO.getDescription());
        role.setStatus(1L);
        roleRepository.save(role);
        List<Long> moduleIds = moduleRepository.existingModule(moduleId);
        if(moduleIds.size() != moduleId.size()) {
            throw new ResourceNotFoundException(MessageConst.LIST_MODULE_NOT_FOUND);
        }
        List<RoleModuleEntity> modules = new ArrayList<>();
        for(Long module : moduleId) {
            RoleModuleEntity moduleEntity = new RoleModuleEntity();
            moduleEntity.setModuleId(module);
            moduleEntity.setRoleId(role.getId());
            moduleEntity.setStaus(1L);
            modules.add(moduleEntity);
        }
        roleModuleReponsitory.saveAll(modules);
        return role;
    }
    public RoleReponseDTO updateRole(RoleRequestDTO requestDTO){
        RoleEntity role = roleRepository.findById(requestDTO.getId()).orElse(null);
        if(role == null){
            return null;
        }
        role.setName(requestDTO.getName());
        role.setDescription(requestDTO.getDescription());
        role.setStatus(requestDTO.getStatus());
        roleRepository.save(role);
        List<Long> moduleIds = moduleRepository.existingModule(requestDTO.getModuleId());
        if(requestDTO.getModuleId().size() != moduleIds.size()){
            throw new ResourceNotFoundException(MessageConst.LIST_ROLE_NOT_FOUND);
        }
        List<RoleModuleEntity> roleModuleEntities = new ArrayList<>();
        for(Long module : moduleIds) {
            RoleModuleEntity roleModuleEntity = new RoleModuleEntity();
            roleModuleEntity.setModuleId(module);
            roleModuleEntity.setRoleId(role.getId());
            roleModuleEntity.setStaus(1L);
            roleModuleEntities.add(roleModuleEntity);
        }
        roleModuleReponsitory.saveAll(roleModuleEntities);
        return toRoleReponseDTO(role,moduleIds);
    }
}
