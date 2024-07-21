package com.fin.finfintech.dto;

import com.fin.finfintech.type.TransactionResultType;
import com.fin.finfintech.type.TransactionType;
import lombok.*;

import java.time.LocalDateTime;

/**
 * {
 * "accountNumber": "1000000001",
 * "transactionType" : "USE"
 * "transactionResult": "S",
 * "transactionId": "137f6a4939d34f08819ea32348314602",
 * "amount": 27000,
 * "transactedAt": "2023-01-18T16:05:38.0978848"
 * }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueryTransactionResponse {

    private String accountNumber;
    private TransactionType transactionType;
    private TransactionResultType transactionResult;
    private String transactionId;
    private Long amount;
    private LocalDateTime transactedAt;

    public static QueryTransactionResponse from(TransactionDto transactionDto) {
        return QueryTransactionResponse.builder()
                .accountNumber(transactionDto.getAccountNumber())
                .transactionType(transactionDto.getTransactionType())
                .transactionResult(transactionDto.getTransactionResultType())
                .transactionId(transactionDto.getTransactionId())
                .amount(transactionDto.getAmount())
                .transactedAt(transactionDto.getTransactedAt())
                .build();
    }
}
