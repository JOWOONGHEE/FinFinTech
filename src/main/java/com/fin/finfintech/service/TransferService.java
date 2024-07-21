package com.fin.finfintech.service;

import com.fin.finfintech.domain.Account;
import com.fin.finfintech.dto.TransferDto;
import com.fin.finfintech.repository.AccountRepository;
import com.fin.finfintech.repository.AccountUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TransferService {

    @Autowired
    private AccountUserRepository accountUserRepository;

    @Autowired
    private AccountRepository accountRepository;

    /**
     * 송금
     * 출금 계좌가 없는 경우
     * 입금 계좌가 없는 경우
     * 잔액이 부족한 경우
     *
     * @param fromAccountNumber
     * @param toAccountNumber
     * @param amount
     * @param memo
     * @return
     */
    @Transactional
    public TransferDto transfer(String fromAccountNumber, String toAccountNumber, Long amount, String memo) {

        Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> new RuntimeException("출금 계좌를 찾을 수 없습니다."));


        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new RuntimeException("입금 계좌를 찾을 수 없습니다."));

        if (fromAccount.getBalance() < amount) {
            throw new RuntimeException("잔액이 부족합니다.");
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);


        return new TransferDto(
                fromAccount.getAccountNumber(),
                toAccount.getAccountNumber(),
                amount,
                memo,
                LocalDateTime.now()
        );
    }
}
