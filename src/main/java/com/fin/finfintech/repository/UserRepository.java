package com.fin.finfintech.repository;

import com.fin.finfintech.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일로 사용자 찾기
    Optional<User> findByEmail(String email);
    // 사용자 이름으로 사용자 찾기
    Optional<User> findByUsername(String username);
}
