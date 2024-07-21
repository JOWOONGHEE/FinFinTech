package com.fin.finfintech.dto;

import com.fin.finfintech.domain.Account;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountInfo {
    private String accountNumber;
    private Long balance;

    public static AccountInfo from(Account account) {
        return AccountInfo.builder()
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .build();
    }
}
