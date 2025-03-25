package com.example.orderproduct.config.security;

import com.example.orderproduct.config.security.filter.JwtAuthFilter;
import com.example.orderproduct.config.security.service.impl.AuthenticationEntryPointImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;

@EnableMethodSecurity
public class AuthConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthFilter jwtConfigurationFilter;
    private final AuthenticationEntryPoint handleSpringSecurityException;

    public AuthConfig(
            AuthenticationProvider authenticationProvider,
            JwtAuthFilter jwtConfigurationFilter,
            @Qualifier("authenticationEntryPointImpl") AuthenticationEntryPointImpl handleSpringSecurityException) {
        this.authenticationProvider = authenticationProvider;
        this.jwtConfigurationFilter = jwtConfigurationFilter;
        this.handleSpringSecurityException = handleSpringSecurityException;
    }

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new SpringSecurityAuditorAware();
    }
}
