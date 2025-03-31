package com.example.orderproduct.service.impl;

import com.example.orderproduct.config.security.dto.UserDetailsImpl;
import com.example.orderproduct.config.security.service.JwtService;
import com.example.orderproduct.constrant.AppConst;
import com.example.orderproduct.constrant.MessageConst;
import com.example.orderproduct.dto.request.LoginRequestDTO;
import com.example.orderproduct.dto.response.BaseReponseDTO;
import com.example.orderproduct.dto.response.LoginReponseDTO;

import com.example.orderproduct.dto.response.RoleReponseDTO;
import com.example.orderproduct.dto.response.UserReponseDTO;
import com.example.orderproduct.entity.RoleEntity;
import com.example.orderproduct.entity.RoleModuleEntity;
import com.example.orderproduct.entity.UserEntity;
import com.example.orderproduct.exception.ResourceNotFoundException;
import com.example.orderproduct.exception.UnauthorizedException;
import com.example.orderproduct.mapper.AuthMapper;
import com.example.orderproduct.mapper.RoleMapper;
import com.example.orderproduct.repository.RoleModuleReponsitory;
import com.example.orderproduct.repository.RoleRepository;
import com.example.orderproduct.repository.UserRepository;
import com.example.orderproduct.service.AuthService;
import com.example.orderproduct.service.MailService;
import com.example.orderproduct.service.PasswordResetTokenService;
import com.example.orderproduct.utils.ResponseUtils;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final AuthMapper authMapper;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final RoleModuleReponsitory roleModuleReponsitory;
    private final RoleMapper roleMapper;
    private final MessageSource messageSource;
    private final PasswordResetTokenService passwordResetTokenService;
    private final MailService mailService;

    @Override
    public BaseReponseDTO<LoginReponseDTO> login(LoginRequestDTO loginRequestDTO,HttpServletResponse httpServletResponse, Locale locale) {
       try{
           String username = loginRequestDTO.getUsername();
           authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(username, loginRequestDTO.getPassword())
           );
           UserEntity user = userRepository.findByUserName(loginRequestDTO.getUsername()).orElse(null);
           List<RoleEntity> roles = roleRepository.findRolesByUserId(user.getId());
           if (roles.isEmpty()) {
               throw new ResourceNotFoundException(MessageConst.ROLE_NOT_FOUND);
           }
           String role = roles.get(0).getName();
           UserDetailsImpl userDetails = UserDetailsImpl.builder()
                   .username(username)
                   .authorities(List.of(new SimpleGrantedAuthority(role)))
                   .build();
           String accessToken = jwtService.generateAccessToken(userDetails);
           String refreshToken = jwtService.generateRefreshToken(userDetails);
           setTokenToCookie(httpServletResponse, accessToken, refreshToken);
           LoginReponseDTO loginResponseDTO = generateLoginResponse(username, role);
           return ResponseUtils.buildResponse(0, MessageConst.LOGIN_SUCCESS, locale, null,loginResponseDTO, messageSource);
       }
       catch (InternalAuthenticationServiceException exception) {
           throw new UnauthorizedException(MessageConst.ACCOUNT_NOT_FOUND);
       } catch (BadCredentialsException exception) {
           throw new UnauthorizedException(MessageConst.USERNAME_PASSWORD_WRONG);
       } catch (DisabledException exception) {
           throw new UnauthorizedException(MessageConst.ACCOUNT_IS_DISABLE);
       } catch (LockedException exception) {
           throw new UnauthorizedException(MessageConst.ACCOUNT_IS_LOCKED);
       }
    }
    private void setTokenToCookie(HttpServletResponse httpServletResponse,
                                  String accessToken, String refreshToken) {
        Cookie refreshTokenCookie = authMapper.createCookie(AppConst.REFRESH_TOKEN, refreshToken);
        Cookie accessTokenCookie = authMapper.createCookie(AppConst.ACCESS_TOKEN, accessToken);
        httpServletResponse.addCookie(refreshTokenCookie);
        httpServletResponse.addCookie(accessTokenCookie);
    }
    private LoginReponseDTO generateLoginResponse(String username, String role) {
        UserEntity user = userRepository
                .findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConst.ACCOUNT_NOT_FOUND));

        RoleEntity roleEntity = roleRepository
                .findByRoleId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageConst.ROLE_NOT_FOUND));
        List<Long> moduleId = roleModuleReponsitory.findByRoleId(roleEntity.getId()).stream().map(RoleModuleEntity::getModuleId).collect(Collectors.toList());
        RoleReponseDTO reponseDTO = roleMapper.toRoleReponseDTO(roleEntity, moduleId);

        return new LoginReponseDTO(
                user.getId(),
                user.getUsername(),
                jwtService.generateAccessToken(UserDetailsImpl.builder()
                        .username(username)
                        .authorities(List.of(new SimpleGrantedAuthority(role)))
                        .build()),
                reponseDTO
        );
    }

    @Override
    public Long getUserIdFromToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        String accessToken = authorizationHeader.substring(7);

        String username = jwtService.extractUsername(accessToken);

        UserEntity user = userRepository
                .findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConst.ACCOUNT_NOT_FOUND));

        return user.getId();
    }
    @Override
    public BaseReponseDTO<UserReponseDTO> getInformation(HttpServletRequest request, Locale locale) {
            try{
                String authorizationHeader = request.getHeader("Authorization");
                String accessToken = authorizationHeader.substring(7); // Loại bỏ "Bearer "
                String username = jwtService.extractUsername(accessToken);
                log.info("#UserName: {}", username);
                UserEntity user = userRepository
                        .findByUserName(username)
                        .orElseThrow(() -> new ResourceNotFoundException(MessageConst.ACCOUNT_NOT_FOUND));
                UserReponseDTO reponse = new UserReponseDTO(
                        user.getUsername(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getImage(),
                        user.getAddress()
                );
                return ResponseUtils.buildResponse(0, MessageConst.GET_DATA_SUCCESS, locale, null,reponse, messageSource);
            } catch (Exception e) {
                log.error("#Error: "+e.getMessage());
                return BaseReponseDTO.<UserReponseDTO>builder()
                        .code(-1)
                        .isSuccess(true)
                        .message(e.getMessage())
                        .data(null)
                        .build();
            }
    }

    @Override
    public BaseReponseDTO<String> logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {
        Arrays.stream(httpServletRequest.getCookies())
            .filter(cookie ->
                    cookie.getName().equals(AppConst.ACCESS_TOKEN) ||
                            cookie.getName().equals(AppConst.REFRESH_TOKEN))
        .forEach(cookie -> {
            cookie.setMaxAge(0);
            cookie.setPath("/");
            httpServletResponse.addCookie(cookie);
        });
        return ResponseUtils.buildResponse(0, MessageConst.LOGOUT_SUCCESS, locale, null,null, messageSource);
    }

    @Override
    public BaseReponseDTO<Object> sentOtpResetPassword(String email, Locale locale) throws MessagingException {
        UserEntity userEntity = userRepository.findByEmail(email).orElse(null);
        if(userEntity== null){
            return ResponseUtils.buildResponse(-1, MessageConst.EMAIL_NOT_FOUND, locale, null,null, messageSource);

        }
        String token = UUID.randomUUID().toString().substring(0,8).toUpperCase();
        Long passwordResetTokenId = passwordResetTokenService.createPasswordResetTokenForUser(userEntity.getId(), token);
        mailService.sendSimpleMail(email, "SenOtp", token);

        return ResponseUtils.buildResponse(0, MessageConst.SENT_OTP_SUCCESS, locale, null,passwordResetTokenId, messageSource);
    }

}
