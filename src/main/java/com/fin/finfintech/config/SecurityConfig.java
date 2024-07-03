package com.fin.finfintech.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/auth/signup", "/auth/login").permitAll() // 인증 없이 접근 가능하도록 설정
                                .anyRequest().authenticated()

                )
                .httpBasic(httpBasic -> { /* 기본 HTTP 인증 설정 */ })
                .formLogin(formLogin -> formLogin.defaultSuccessUrl("/", true)); // 기본 폼 로그인 설정 및 로그인 성공 시 이동할 URL 설정

        return http.build();
    }
}

