package com.fin.finfintech.repository;

import com.fin.finfintech.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 회원 이름 존재 여부
     *
     * @param email
     * @return
     */
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}