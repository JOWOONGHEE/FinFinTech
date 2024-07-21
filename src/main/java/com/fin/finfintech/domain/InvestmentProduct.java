package com.fin.finfintech.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class InvestmentProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String riskLevel; // 리스크 수준
    private String description;
    private BigDecimal expectedReturnRate; // 기대 수익률
}
