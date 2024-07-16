package com.fin.finfintech.service;

import com.fin.finfintech.domain.AccountUser;
import com.fin.finfintech.domain.User;
import com.fin.finfintech.dto.Auth;
import com.fin.finfintech.repository.AccountUserRepository;
import com.fin.finfintech.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AccountUserRepository accountUserRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("회원 정보를 찾을 수 없습니다."));

        return null;
    }

    /**
     * 회원 가입
     *
     * @param user
     * @return
     */
    @Transactional
    public User register(Auth.SignUp user) {
        boolean exists = this.userRepository.existsByEmail(user.getEmail());

        // 이메일 중복 체크
        if (exists) {
            throw new RuntimeException("이미 존재하는 사용자입니다.");
        }

        // 비밀번호 암호화
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        // user 테이블에 저장
        User savedUser = this.userRepository.save(user.toEntity());

        // account_user 테이블에 저장
        AccountUser accountUser = AccountUser.builder()
                .id(savedUser.getId())
                .name(savedUser.getUsername())
                .build();


        this.accountUserRepository.save(accountUser);

        return savedUser;
    }

    /**
     * 인증
     *
     * @param user
     * @return
     */
    public User authenticate(Auth.SignIn user) {
        var result = this.userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 email 입니다."));

        if (!this.passwordEncoder.matches(user.getPassword(), result.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return result;
    }
}
