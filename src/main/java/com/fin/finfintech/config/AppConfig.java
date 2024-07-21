package com.fin.finfintech.config;


import com.fin.finfintech.security.TokenProvider;
import com.fin.finfintech.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TokenProvider tokenProvider(UserService userService) {
        return new TokenProvider(userService);
    }
}