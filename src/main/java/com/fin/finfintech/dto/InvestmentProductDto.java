package com.fin.finfintech.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Builder
public class InvestmentProductDto {
    private Long id;
    private String name;
    private String riskLevel; // 리스크 수준
    private String description;
    private BigDecimal expectedReturnRate; // 기대 수익률

    public InvestmentProductDto(Long id, String name, String riskLevel, String description, BigDecimal expectedReturnRate) {
        this.id = id;
        this.name = name;
        this.riskLevel = riskLevel;
        this.description = description;
        this.expectedReturnRate = expectedReturnRate;
    }
}
