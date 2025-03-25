package com.example.orderproduct.service.impl;

import com.example.orderproduct.config.security.dto.UserDetailsImpl;
import com.example.orderproduct.config.security.service.JwtService;
import com.example.orderproduct.constrant.AppConst;
import com.example.orderproduct.constrant.MessageConst;
import com.example.orderproduct.dto.request.LoginRequestDTO;
import com.example.orderproduct.dto.response.BaseReponseDTO;
import com.example.orderproduct.dto.response.LoginReponseDTO;

import com.example.orderproduct.dto.response.RoleReponseDTO;
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
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
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
    private final PasswordEncoder passwordEncoder;

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
           return BaseReponseDTO.<LoginReponseDTO>builder()
                   .code(0)
                   .isSuccess(true)
                   .data(loginResponseDTO)
                   .build();
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
}
