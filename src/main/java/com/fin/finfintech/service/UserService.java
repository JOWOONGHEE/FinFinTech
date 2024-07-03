package com.fin.finfintech.service;

import com.fin.finfintech.domain.LoginRequest;
import com.fin.finfintech.domain.SignupRequest;
import com.fin.finfintech.entity.User;
import com.fin.finfintech.exception.CustomException;
import com.fin.finfintech.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 회원가입 처리
    public ResponseEntity<?> registerUser(SignupRequest signupRequest) {
        if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email is already taken!");
        }

        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setEmail(signupRequest.getEmail());
        user.setBirthdate(signupRequest.getBirthdate());
        user.setPhone(signupRequest.getPhone());

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    // 로그인 처리
    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new CustomException("User not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }

        return ResponseEntity.ok("User authenticated successfully");
    }
}
