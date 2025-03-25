package com.example.orderproduct.config.security.service.impl;

import com.example.orderproduct.config.security.dto.UserDetailsImpl;
import com.example.orderproduct.entity.RoleEntity;
import com.example.orderproduct.entity.UserEntity;
import com.example.orderproduct.repository.RoleRepository;
import com.example.orderproduct.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        List<GrantedAuthority> authorities = getPermissionAndSetToListGrantedAuthority(user);

        return new UserDetailsImpl(user.getUsername(), user.getPassword(), authorities);
    }
    @NotNull
    private List<GrantedAuthority> getPermissionAndSetToListGrantedAuthority(UserEntity accountExisted) {
        RoleEntity userRole = roleRepository.findByRoleId(accountExisted.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with role: " + accountExisted.getId()));
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_".concat(userRole.getName())));

        return authorities;
    }

}
