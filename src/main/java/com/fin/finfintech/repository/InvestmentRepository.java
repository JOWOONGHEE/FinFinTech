package com.fin.finfintech.repository;

import com.fin.finfintech.domain.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Long> {
    // 필요 시 추가적인 쿼리 메서드를 정의할 수 있습니다.
    Optional<Investment> findById(Long id);
}
