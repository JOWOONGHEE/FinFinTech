package com.fin.finfintech.dto;

import com.fin.finfintech.aop.AccountLockIdInterface;
import com.fin.finfintech.type.TransactionResultType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

public class CancelBalance {

    /**
     * {
     * "transactionId" : "30807e768c3f4a9ca9195456ed515269",
     * "accountNumber" : 1000000001,
     * "amount" : 27000
     * }
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request implements AccountLockIdInterface {
        @NotBlank
        private String transactionId;

        @NotBlank
        @Size(min = 10, max = 10)
        private String accountNumber;

        @NotNull
        @Min(10)
        @Max(1000_000_000)
        private Long amount;
    }

    /**
     * {
     * "accountNumber": "1000000001",
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
    public static class Response {
        private String accountNumber;
        private TransactionResultType transactionResult;
        private String transactionId;
        private Long amount;
        private LocalDateTime transactedAt;

        public static Response from(TransactionDto transactionDto) {
            return Response.builder()
                    .accountNumber(transactionDto.getAccountNumber())
                    .transactionResult(transactionDto.getTransactionResultType())
                    .transactionId(transactionDto.getTransactionId())
                    .amount(transactionDto.getAmount())
                    .transactedAt(transactionDto.getTransactedAt())
                    .build();
        }
    }
}
