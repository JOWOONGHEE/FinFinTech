package com.fin.finfintech.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@Builder
public class TransferDto {
    private String fromAccountNumber;
    private String toAccountNumber;
    private Long amount;
    private String memo;
    private LocalDateTime transferDate;

    public TransferDto(String fromAccountNumber, String toAccountNumber, Long amount, String memo, LocalDateTime transferDate) {
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.amount = amount;
        this.memo = memo;
        this.transferDate = transferDate;
    }
}
