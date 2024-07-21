package com.fin.finfintech.repository;

import com.fin.finfintech.domain.AutoTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutoTransferRepository extends JpaRepository<AutoTransfer, Long> {
    // 추가적인 쿼리 메서드가 필요하다면 여기에 정의
}
