package com.fin.finfintech.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferDto {
    private String fromAccountNumber;
    private String toAccountNumber;
    private Long amount;
}
