package com.meddoc.meddoc.config;

import com.meddoc.meddoc.service.UsuarioDetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import java.io.IOException;
import java.util.Collection;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UsuarioDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    public void configure(AuthenticationManagerBuilder auth, DaoAuthenticationProvider authProvider) {
        auth.authenticationProvider(authProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/login",
                                "/cadastro",       // <- necessário
                                "/cadastro/**",    // <- necessário
                                "/recuperar-senha",
                                "/recuperar-senha/**",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/"
                        ).permitAll()
                        .requestMatchers("/idoso/**").hasRole("IDOSO")
                        .requestMatchers("/medico/**").hasRole("MEDICO")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(authenticationSuccessHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
                Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_IDOSO"))) {
                    response.sendRedirect(request.getContextPath() + "/idoso/dashboard");
                } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_MEDICO"))) {
                    response.sendRedirect(request.getContextPath() + "/medico/dashboard");
                } else {
                    throw new IllegalStateException("Tipo de usuário desconhecido");
                }
            }
        };
    }
}
