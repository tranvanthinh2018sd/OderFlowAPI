package com.example.orderproduct.service.impl;

import com.example.orderproduct.constrant.MessageConst;
import com.example.orderproduct.dto.request.UserRequestDTO;
import com.example.orderproduct.dto.response.BaseReponseDTO;
import com.example.orderproduct.dto.response.PaginationReponseDTO;
import com.example.orderproduct.dto.response.UserReponseDTO;
import com.example.orderproduct.entity.UserEntity;
import com.example.orderproduct.entity.UserRoleEnity;
import com.example.orderproduct.mapper.UserMapper;
import com.example.orderproduct.repository.UserRepository;
import com.example.orderproduct.repository.UserRoleRepository;
import com.example.orderproduct.service.MailService;
import com.example.orderproduct.service.UserService;
import com.example.orderproduct.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.orderproduct.utils.CommonUtils.convertToBase64;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MailService mailService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public BaseReponseDTO<List<UserReponseDTO>> getAllUserPaging(String search,int page, int size, Locale locale) {
        try {
            int offset = page * size;
            List<UserEntity> reponse = userRepository.findAll(offset,size);

            int totalElement = userRepository.countUserPagin();
            int totalPage = (int) Math.ceil((double) totalElement / size);

            PaginationReponseDTO paginationReponseDTO = PaginationReponseDTO.builder()
                    .currentPage(page)
                    .size(size)
                    .totalItems(totalElement)
                    .totalPages(totalPage)
                    .build();

            if(reponse.isEmpty()){
                return BaseReponseDTO.<List<UserReponseDTO>>builder()
                        .code(-1)
                        .isSuccess(false)
                        .message(messageSource.getMessage(MessageConst.LIST_USER_NOT_FOUND,null,locale))
                        .data(null)
                        .build();
            }
            else {
                List<UserReponseDTO> userReponseDTOS = reponse.stream().map(userMapper::toUserGetResponseDTO).collect(Collectors.toList());
                return BaseReponseDTO.<List<UserReponseDTO>>builder()
                        .code(0)
                        .isSuccess(true)
                        .message(messageSource.getMessage(MessageConst.GET_DATA_SUCCESS,null, locale))
                        .pagination(paginationReponseDTO)
                        .data(userReponseDTOS)
                        .build();
            }
        }
        catch (Exception e){
            return BaseReponseDTO.<List<UserReponseDTO>>builder()
                    .code(-2)
                    .isSuccess(false)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @Override
    public BaseReponseDTO<UserReponseDTO> getUserById(Long id, Locale locale) {
        try{
            UserEntity userEntity = userRepository.findById(id).orElse(null);
            if(userEntity == null) {
                return BaseReponseDTO.<UserReponseDTO>builder()
                        .code(-1)
                        .isSuccess(false)
                        .message(messageSource.getMessage(MessageConst.USER_NOT_FOUND,null,locale))
                        .data(null)
                        .build();
            }
            List<Long> roleId = userRoleRepository.findByUserId(userEntity.getId()).stream().map(UserRoleEnity::getRoleId).collect(Collectors.toList());
            UserReponseDTO userReponseDTO = userMapper.toUserReponseDTO(userEntity,roleId);

                return BaseReponseDTO.<UserReponseDTO>builder()
                        .code(0)
                        .isSuccess(true)
                        .message(messageSource.getMessage(MessageConst.GET_DATA_SUCCESS,null, locale))
                        .data(userReponseDTO)
                        .build();

        }catch (Exception e){
            return BaseReponseDTO.<UserReponseDTO>builder()
                    .code(-2)
                    .isSuccess(false)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }
    @Override
    public BaseReponseDTO<UserReponseDTO> createUser(UserRequestDTO request, MultipartFile imageRequest, Locale locale) throws IOException {
        try{
            String image = CommonUtils.convertToBase64(imageRequest);
            request.setImage(image);
            Optional<UserEntity> user = userRepository.findByUserName(request.getUsername());
            if(user.isPresent()) {
                return BaseReponseDTO.<UserReponseDTO>builder()
                        .code(-1)
                        .isSuccess(false)
                        .message(messageSource.getMessage(MessageConst.USER_ALREDY_EXISTS, null, locale))
                        .data(null)
                        .build();
            }
            request.setPassword(passwordEncoder.encode(request.getPassword()));
            UserReponseDTO reponse = userMapper.createUser(request, request.getRoleId());
            if(reponse.getId() != null){
                mailService.sendComfirmLink(reponse.getEmail(),reponse.getId(), "sercretCode");
            }
            return BaseReponseDTO.<UserReponseDTO>builder()
                    .code(0)
                    .isSuccess(true)
                    .message(messageSource.getMessage(MessageConst.CREATE_USER_SUCCESS, null, locale))
                    .data(reponse)
                    .build();

        } catch (Exception e) {
            return BaseReponseDTO.<UserReponseDTO>builder()
                    .code(-2)
                    .isSuccess(false)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }
    @Override
    public BaseReponseDTO<UserReponseDTO> updateUser(UserRequestDTO request, Locale locale) {
       try {
           UserEntity userEntity = userRepository.findById(request.getId()).orElse(null);
           if(userEntity == null){
               return BaseReponseDTO.<UserReponseDTO>builder()
                       .code(-1)
                       .isSuccess(false)
                       .message(messageSource.getMessage(MessageConst.USER_NOT_FOUND, null, locale))
                       .data(null)
                       .build();
           }
           UserReponseDTO reponse = userMapper.updateUser(request, request.getRoleId());
               return BaseReponseDTO.<UserReponseDTO>builder()
                       .code(0)
                       .isSuccess(true)
                       .message(messageSource.getMessage(MessageConst.UPDATE_USER_SUCCESS, null, locale))
                       .data(reponse)
                       .build();
       } catch (Exception e) {
           return BaseReponseDTO.<UserReponseDTO>builder()
                   .code(-2)
                   .isSuccess(false)
                   .message(e.getMessage())
                   .data(null)
                   .build();
       }
    }
    @Override
    public BaseReponseDTO< String> confirmUser(Long userId, String verifyCode) {
        return BaseReponseDTO.<String>builder()
                .code(0)
                .isSuccess(true)
                .message("Confirmed!")
                .data(null)
                .build();
    }
}
