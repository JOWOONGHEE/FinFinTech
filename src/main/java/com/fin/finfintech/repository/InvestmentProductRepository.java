package com.fin.finfintech.repository;

import com.fin.finfintech.domain.InvestmentProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvestmentProductRepository extends JpaRepository<InvestmentProduct, Long> {
    // 특정 ID로 투자 상품 조회
    Optional<InvestmentProduct> findById(Long id);
}
