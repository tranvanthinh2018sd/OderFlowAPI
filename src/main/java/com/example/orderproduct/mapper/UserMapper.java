package com.example.orderproduct.mapper;

import com.example.orderproduct.constrant.MessageConst;
import com.example.orderproduct.dto.request.UserRequestDTO;
import com.example.orderproduct.dto.response.RoleReponseDTO;
import com.example.orderproduct.dto.response.UserReponseDTO;
import com.example.orderproduct.entity.UserEntity;
import com.example.orderproduct.entity.UserRoleEnity;
import com.example.orderproduct.exception.ResourceNotFoundException;
import com.example.orderproduct.repository.RoleRepository;
import com.example.orderproduct.repository.UserRepository;
import com.example.orderproduct.repository.UserRoleRepository;
import com.example.orderproduct.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class UserMapper {
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public UserReponseDTO toUserGetResponseDTO(UserEntity userEntity) {
        Long id = userEntity.getId();
        String username = userEntity.getUsername();
        String email = userEntity.getEmail();
        String phone = userEntity.getPhone();
        String image = userEntity.getImage();
        String address = userEntity.getAddress();
        Long status = userEntity.getStatus();
        return new UserReponseDTO(id,username,email,phone,image,address,status);
    }

    public UserReponseDTO toUserReponseDTO(UserEntity user, List<Long> roleId) {
        UserReponseDTO userReponseDTO = new UserReponseDTO();
        userReponseDTO.setId(user.getId());
        userReponseDTO.setUsername(user.getUsername());
        userReponseDTO.setEmail(user.getEmail());
        userReponseDTO.setPhone(user.getPhone());
        userReponseDTO.setAddress(user.getAddress());
        userReponseDTO.setImage(user.getImage());
        List<RoleReponseDTO> roleReponseDTO = new ArrayList<>();
        for(Long role : roleId) {
             roleRepository.findById(role).ifPresent(roleReponse -> {
                 RoleReponseDTO roleReponseDTO1 = new RoleReponseDTO();
                 roleReponseDTO1.setId(roleReponse.getId());
                 roleReponseDTO1.setName(roleReponse.getName());
                 roleReponseDTO1.setDescription(roleReponse.getDescription());
                 roleReponseDTO.add(roleReponseDTO1);
             });
        }
        userReponseDTO.setRoles(roleReponseDTO);
        return userReponseDTO;
    }

    public UserReponseDTO createUser(UserRequestDTO request, List<Long> roleId) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(request.getUsername());
        userEntity.setPassword(request.getPassword());
        userEntity.setEmail(request.getEmail());
        userEntity.setPhone(request.getPhone());
        userEntity.setAddress(request.getAddress());
        userEntity.setImage(request.getImage());
        userEntity.setCreaDate(new Date());
        userEntity.setStatus(1L);
        userRepository.save(userEntity);
        List<Long> roleIds = roleRepository.existingRoles(roleId);
        if(roleIds.size() != roleId.size()){
            throw new ResourceNotFoundException(MessageConst.LIST_ROLE_NOT_FOUND);
        }
        List<UserRoleEnity> userRoles = new ArrayList<>();
        for(Long role : roleId){
            UserRoleEnity userRoleEnity = new UserRoleEnity();
            userRoleEnity.setRoleId(role);
            userRoleEnity.setUserId(userEntity.getId());
            userRoles.add(userRoleEnity);
        }
        userRoleRepository.saveAll(userRoles);

        return toUserReponseDTO(userEntity, roleId);
    }
    public UserReponseDTO updateUser(UserRequestDTO requestDTO, List<Long> roleId) {
        UserEntity userEntity = userRepository.findById(requestDTO.getId()).orElse(null);
        if(userEntity == null){
            return null;
        }
        if (requestDTO.getUsername() != null) {
            userEntity.setUsername(requestDTO.getUsername());
        }
        if (requestDTO.getPassword() != null) {
            userEntity.setPassword(requestDTO.getPassword());
        }
        if (requestDTO.getEmail() != null) {
            userEntity.setEmail(requestDTO.getEmail());
        }
        if (requestDTO.getPhone() != null) {
            userEntity.setPhone(requestDTO.getPhone());
        }
        if (requestDTO.getAddress() != null) {
            userEntity.setAddress(requestDTO.getAddress());
        }
        if (requestDTO.getImage() != null) {
            userEntity.setImage(requestDTO.getImage());
        }
        userEntity.setStatus(1L);
        userEntity.setUpdateDate(new Date());
        userRepository.save(userEntity);
        List<Long> existingRoleId = roleRepository.existingRoles(roleId);
        if(roleId.size() != existingRoleId.size()){
            throw new ResourceNotFoundException(MessageConst.LIST_ROLE_NOT_FOUND);
        }
            userRoleRepository.deleteByUserId(requestDTO.getId());
            List<UserRoleEnity> userRoles = new ArrayList<>();
            for(Long role : roleId){
                UserRoleEnity userRoleEnity = new UserRoleEnity();
                userRoleEnity.setRoleId(role);
                userRoleEnity.setUserId(userEntity.getId());
                userRoles.add(userRoleEnity);
            }
            userRoleRepository.saveAll(userRoles);
        return toUserReponseDTO(userEntity, roleId);
    }
}
