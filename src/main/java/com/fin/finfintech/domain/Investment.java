package com.fin.finfintech.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Investment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "investment_product_id")
    private InvestmentProduct product; // 투자상품 정보

    private String accountNumber; // 계좌 번호
    private Long amount; // 투자 금액
    private LocalDateTime startDate; // 시작 날짜
    private LocalDateTime endDate; // 종료 날짜
}
