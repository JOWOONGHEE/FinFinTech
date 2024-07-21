package com.fin.finfintech.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
public class AutoTransferDto {
    private String fromAccountNumber;
    private String toAccountNumber;
    private Long amount;
    private String memo;
    private LocalDateTime transferDate;

    private LocalDateTime scheduleStartDateTime;
    private String frequency;

    public AutoTransferDto(String fromAccountNumber, String toAccountNumber,
                           Long amount, String memo, LocalDateTime transferDate,
                           LocalDateTime scheduleStartDateTime, String frequency) {
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.amount = amount;
        this.memo = memo;
        this.transferDate = transferDate;
        this.scheduleStartDateTime = scheduleStartDateTime;
        this.frequency = frequency;
    }
    // 추가적으로 필요한 필드나 메서드가 있다면 여기에 추가
}
