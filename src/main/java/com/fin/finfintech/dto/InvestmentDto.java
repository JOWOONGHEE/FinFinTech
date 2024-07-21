package com.fin.finfintech.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
public class InvestmentDto {
    private Long id;
    private String accountNumber; // 계좌 번호
    private Long productId; // 투자상품 ID
    private Long amount; // 투자 금액
    private LocalDateTime startDate; // 시작 날짜
    private LocalDateTime endDate; // 종료 날짜

    public InvestmentDto(Long id, String accountNumber, Long productId, Long amount, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.productId = productId;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
