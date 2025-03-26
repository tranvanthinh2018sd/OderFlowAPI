package com.example.orderproduct.config.security.filter;

import com.example.orderproduct.config.security.dto.UserDetailsImpl;
import com.example.orderproduct.config.security.service.JwtService;
import com.example.orderproduct.config.security.service.impl.UserDetailsServiceImpl;
import com.example.orderproduct.constrant.AppConst;
import com.example.orderproduct.constrant.MessageConst;
import com.example.orderproduct.dto.response.BaseReponseDTO;
import com.example.orderproduct.entity.UserEntity;
import com.example.orderproduct.exception.ResourceNotFoundException;
import com.example.orderproduct.exception.UnauthorizedException;
import com.example.orderproduct.mapper.AuthMapper;
import com.example.orderproduct.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    private final MessageSource messageSource;
    private final AuthMapper authMapper;

    private static final List<String> AUTH_WHITELIST = Arrays.asList(
            "/api/v1/images",
            "/api/v1/auth/login",
            "/api/v1/common/send-email"
    );
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String servletPath = request.getServletPath();


        if(AUTH_WHITELIST.stream().anyMatch(item -> item.contains(servletPath))) {
            filterChain.doFilter(request, response);
            return;
        }

        if(servletPath.contains("/upload-file")) {
            filterChain.doFilter(request, response);
            return;
        }

        if(servletPath.contains("/images/")) {
            filterChain.doFilter(request, response);
            return;
        }
        if(servletPath.contains("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String authorizationHeader = request.getHeader("Authorization");

            if (StringUtils.isBlank(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
                throw new UnauthorizedException(MessageConst.JWT_REQUIRED);
            }

            String accessToken = authorizationHeader.substring(7); // Loại bỏ "Bearer "

            if (StringUtils.isBlank(accessToken))
            {
                throw new UnauthorizedException(MessageConst.JWT_REQUIRED);
            }

            String username = jwtService.extractUsername(accessToken);
            UserEntity user = userRepository.findByUserName(username).orElseThrow(
                    () -> new ResourceNotFoundException(MessageConst.ACCOUNT_NOT_FOUND)
            );

            if (user.getStatus() == 2 || user.getStatus() == 0) {
                throw new ResourceNotFoundException(MessageConst.ACCOUNT_NOT_FOUND);
            }

            Boolean isAccessToken = jwtService.extractIsAccessToken(accessToken);
            if (Boolean.FALSE.equals(isAccessToken))
            {
                throw new JwtException(MessageConst.JWT_REQUIRED);
            }

            authorizationUser(request, response, filterChain, username, accessToken);
        } catch (UnauthorizedException exception) {
            responseException(request, response, HttpStatus.UNAUTHORIZED, exception.getMessage());
        } catch (ExpiredJwtException ex) {
            responseException(request, response, HttpStatus.UNAUTHORIZED ,MessageConst.JWT_EXPIRED);
        } catch (JwtException ex) {
            responseException(request, response, HttpStatus.UNAUTHORIZED, MessageConst.JWT_UNAUTHORIZED);
        } catch (ResourceNotFoundException ex) {
            responseException(request, response, HttpStatus.UNAUTHORIZED, MessageConst.ACCOUNT_NOT_FOUND);
        }  catch (Exception ex) {
            responseException(request, response, HttpStatus.INTERNAL_SERVER_ERROR, MessageConst.INTERNAL_SERVER_ERROR);
        }
    }

    private void handleRefreshToken(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        Optional<Cookie> refreshTokenCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> AppConst.REFRESH_TOKEN.equals(cookie.getName()))
                .findFirst();
        try {
            if (refreshTokenCookie.isPresent())
            {
                String refreshToken = refreshTokenCookie.get().getValue();
                Boolean isAccessToken = jwtService.extractIsAccessToken(refreshToken);
                String username = jwtService.extractUsername(refreshToken);
                if (Boolean.FALSE.equals(isAccessToken))
                {
                    String newAccessToken = jwtService.generateAccessToken(
                            UserDetailsImpl.builder()
                                    .username(username)
                                    .build());
                    Arrays.stream(request.getCookies())
                            .filter(cookie -> cookie.getName().equals(AppConst.ACCESS_TOKEN))
                            .findFirst()
                            .ifPresent(cookie -> cookie.setValue(newAccessToken));
                    Cookie accessTokenCookie = authMapper.createCookie(AppConst.ACCESS_TOKEN, newAccessToken);
                    response.addCookie(accessTokenCookie);
                    authorizationUser(request, response, filterChain, username, newAccessToken);
                    return;
                }
            }
            throw new ExpiredJwtException(null, null, MessageConst.JWT_EXPIRED);
        } catch (ExpiredJwtException ex) {
            responseException(request, response, HttpStatus.UNAUTHORIZED ,MessageConst.JWT_EXPIRED);
        }
    }

    private void authorizationUser(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain,
                                   String username, String accessToken) throws IOException, ServletException, ExpiredJwtException {

        List<String> roles = jwtService.extractRoles(accessToken);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);


            if (jwtService.isTokenInvalid(accessToken, userDetails)) {

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }


    private void responseException(HttpServletRequest request, HttpServletResponse response,
                                   HttpStatus httpStatus, String errorMessage) throws IOException {
        Locale locale;
        if(request.getLocale() == null) {
            locale = Locale.US;
        } else {
            locale = request.getLocale();
        }

        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        BaseReponseDTO<Object> serviceResponse = BaseReponseDTO.<Object>builder()
                .isSuccess(Boolean.TRUE)
                .message(messageSource.getMessage(errorMessage, null,
                        "Jwt required",
                        locale))
                .code(AppConst.STATUS_FAIL)
                .build();

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), serviceResponse);
    }
}
