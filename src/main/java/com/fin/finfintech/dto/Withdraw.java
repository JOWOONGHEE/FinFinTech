package com.fin.finfintech.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

public class Withdraw {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotNull
        private Long userId;
        @NotNull
        private String accountNumber;
        @NotNull
        private Long amount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String accountNumber;
        private Long amount;
        private Long balanceSnapshot;
        private String transactionId;
        private LocalDateTime transactedAt;

        public static Response from(String accountNumber, Long amount, Long updatedBalance, String transactionId, LocalDateTime transactedAt) {
            return Response.builder()
                    .accountNumber(accountNumber)
                    .amount(amount)
                    .balanceSnapshot(updatedBalance)
                    .transactionId(transactionId)
                    .transactedAt(transactedAt)
                    .build();
        }
    }
}
