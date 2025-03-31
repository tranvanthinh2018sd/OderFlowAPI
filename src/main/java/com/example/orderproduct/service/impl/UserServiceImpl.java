package com.example.orderproduct.service.impl;

import com.example.orderproduct.constrant.MessageConst;
import com.example.orderproduct.dto.request.UserRequestDTO;
import com.example.orderproduct.dto.response.BaseReponseDTO;
import com.example.orderproduct.dto.response.PaginationReponseDTO;
import com.example.orderproduct.dto.response.UserReponseDTO;
import com.example.orderproduct.entity.PasswordResetTokenEntity;
import com.example.orderproduct.entity.UserEntity;
import com.example.orderproduct.entity.UserRoleEnity;
import com.example.orderproduct.mapper.UserMapper;
import com.example.orderproduct.repository.PasswordResetTokenRepository;
import com.example.orderproduct.repository.UserRepository;
import com.example.orderproduct.repository.UserRoleRepository;
import com.example.orderproduct.service.UserService;
import com.example.orderproduct.utils.CommonUtils;
import com.example.orderproduct.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.orderproduct.utils.ResponseUtils.buildResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserRoleRepository userRoleRepository;
    private final MessageSource messageSource;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;


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
                return ResponseUtils.buildResponse(-1, MessageConst.LIST_USER_NOT_FOUND, locale, null,null, messageSource);
            }
            else {
                List<UserReponseDTO> userReponseDTOS = reponse.stream().map(userMapper::toUserGetResponseDTO).collect(Collectors.toList());

                return ResponseUtils.buildResponse(0, MessageConst.GET_DATA_SUCCESS, locale, paginationReponseDTO,userReponseDTOS, messageSource);

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
                return buildResponse(-1, MessageConst.USER_NOT_FOUND, locale, null,null, messageSource);
            }
            List<Long> roleId = userRoleRepository.findByUserId(userEntity.getId()).stream().map(UserRoleEnity::getRoleId).collect(Collectors.toList());
            UserReponseDTO userReponseDTO = userMapper.toUserReponseDTO(userEntity,roleId);

                return ResponseUtils.buildResponse(0, MessageConst.GET_DATA_SUCCESS, locale, null,userReponseDTO, messageSource);

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
                return ResponseUtils.buildResponse(-1, MessageConst.USER_ALREDY_EXISTS, locale, null,null, messageSource);
            }
            request.setPassword(passwordEncoder.encode(request.getPassword()));
            UserReponseDTO reponse = userMapper.createUser(request, request.getRoleId());
            return ResponseUtils.buildResponse(0, MessageConst.CREATE_USER_SUCCESS, locale, null,reponse, messageSource);

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
    public BaseReponseDTO<UserReponseDTO> updateUser(UserRequestDTO request, MultipartFile fileImage, Locale locale) {
       try {
           UserEntity userEntity = userRepository.findById(request.getId()).orElse(null);
           if(userEntity == null){
               return ResponseUtils.buildResponse(-1, MessageConst.USER_NOT_FOUND, locale, null,null, messageSource);
           }
           String image = CommonUtils.convertToBase64(fileImage);
           request.setImage(image);
           UserReponseDTO reponse = userMapper.updateUser(request, request.getRoleId());
           return ResponseUtils.buildResponse(0, MessageConst.UPDATE_USER_SUCCESS, locale, null,reponse, messageSource);
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
    public BaseReponseDTO< String> confirmUser(Long userId, String verifyCode, Locale locale) {
        return ResponseUtils.buildResponse(0, MessageConst.CONFIRM, locale, null,null, messageSource);
    }
    @Override
    public BaseReponseDTO<Object> savePassswordForOTP(Long passwordResetTokenId, String token, String password, Locale locale) {
        try {
            PasswordResetTokenEntity passwordResetTokenEntity = passwordResetTokenRepository.findById(passwordResetTokenId).orElse(null);
            if (passwordResetTokenEntity == null) {
                return ResponseUtils.buildResponse(-1, MessageConst.ID_NOT_FOUND, locale, null,null, messageSource);
            }

            Date expiryDate = passwordResetTokenEntity.getExpiryDate();
            if (expiryDate == null || expiryDate.before(new Date())) {
                return ResponseUtils.buildResponse(-1, MessageConst.TOKEN_EXPIRED, locale, null,null, messageSource);
            }

            if (!passwordResetTokenEntity.getToken().equals(token)) {
                return ResponseUtils.buildResponse(-2, MessageConst.TOKEN_INVALID, locale, null,null, messageSource);
            }

            UserEntity userEntity = userRepository.findById(passwordResetTokenEntity.getUserId()).orElse(null);
            if (userEntity == null) {
                return ResponseUtils.buildResponse(-3, MessageConst.USER_NOT_FOUND, locale, null,null, messageSource);
            }

            userEntity.setPassword(passwordEncoder.encode(password));
            userRepository.save(userEntity);

            return ResponseUtils.buildResponse(0, MessageConst.SAVE_SUCCESS, locale, null,null, messageSource);

        } catch (Exception e) {
            return BaseReponseDTO.<Object>builder()
                    .code(-4)
                    .isSuccess(false)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }


}
