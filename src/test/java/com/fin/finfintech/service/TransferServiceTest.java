package com.fin.finfintech.service;

import com.fin.finfintech.domain.Account;
import com.fin.finfintech.dto.TransferDto;
import com.fin.finfintech.repository.AccountRepository;
import com.fin.finfintech.repository.AccountUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferServiceTest {

    @InjectMocks
    private TransferService transferService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountUserRepository accountUserRepository;

    private Account fromAccount;
    private Account toAccount;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        fromAccount = new Account();
        fromAccount.setAccountNumber("1234567890");
        fromAccount.setBalance(5000L);

        toAccount = new Account();
        toAccount.setAccountNumber("0987654321");
        toAccount.setBalance(2000L);
    }

    @Test
    void testTransfer_Success() {
        when(accountRepository.findByAccountNumber("1234567890")).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByAccountNumber("0987654321")).thenReturn(Optional.of(toAccount));

        TransferDto result = transferService.transfer("1234567890", "0987654321", 1000L, "Test Transfer");

        assertNotNull(result);
        assertEquals(4000L, fromAccount.getBalance());
        assertEquals(3000L, toAccount.getBalance());
        assertEquals("1234567890", result.getFromAccountNumber());
        assertEquals("0987654321", result.getToAccountNumber());
        assertEquals(1000L, result.getAmount());
        assertEquals("Test Transfer", result.getMemo());

        verify(accountRepository, times(1)).save(fromAccount);
        verify(accountRepository, times(1)).save(toAccount);
    }

    @Test
    void testTransfer_FromAccountNotFound() {
        when(accountRepository.findByAccountNumber("1234567890")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                transferService.transfer("1234567890", "0987654321", 1000L, "Test Transfer"));

        assertEquals("출금 계좌를 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    void testTransfer_ToAccountNotFound() {
        when(accountRepository.findByAccountNumber("1234567890")).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByAccountNumber("0987654321")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                transferService.transfer("1234567890", "0987654321", 1000L, "Test Transfer"));

        assertEquals("입금 계좌를 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    void testTransfer_InsufficientBalance() {
        when(accountRepository.findByAccountNumber("1234567890")).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByAccountNumber("0987654321")).thenReturn(Optional.of(toAccount));

        Exception exception = assertThrows(RuntimeException.class, () ->
                transferService.transfer("1234567890", "0987654321", 6000L, "Test Transfer"));

        assertEquals("잔액이 부족합니다.", exception.getMessage());
    }
}
