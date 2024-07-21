package com.fin.finfintech.service;
import com.fin.finfintech.domain.Account;
import com.fin.finfintech.domain.AutoTransfer;
import com.fin.finfintech.dto.AutoTransferDto;
import com.fin.finfintech.repository.AccountRepository;
import com.fin.finfintech.repository.AutoTransferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutoTransferServiceTest {

    @InjectMocks
    private AutoTransferService autoTransferService;

    @Mock
    private TransferService transferService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AutoTransferRepository autoTransferRepository;

    private Account fromAccount;
    private Account toAccount;
    private AutoTransferDto autoTransferDto;
    private AutoTransfer autoTransfer;

    @BeforeEach
    void setUp() {
        fromAccount = new Account();
        fromAccount.setAccountNumber("12345");

        toAccount = new Account();
        toAccount.setAccountNumber("54321");

        autoTransferDto = AutoTransferDto.builder()
                .fromAccountNumber("12345")
                .toAccountNumber("54321")
                .amount(1000L)
                .memo("Test Memo")
                .scheduleStartDateTime(LocalDateTime.now())
                .frequency("DAILY")
                .build();

        autoTransfer = AutoTransfer.builder()
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .amount(1000L)
                .memo("Test Memo")
                .scheduleStartDateTime(LocalDateTime.now())
                .frequency("DAILY")
                .build();
    }

    @Test
    void testScheduleAutoTransfer() {
        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByAccountNumber("54321")).thenReturn(Optional.of(toAccount));
        when(autoTransferRepository.save(any(AutoTransfer.class))).thenReturn(autoTransfer);

        AutoTransferDto result = autoTransferService.scheduleAutoTransfer(autoTransferDto);

        assertNotNull(result);
        assertEquals("12345", result.getFromAccountNumber());
        assertEquals("54321", result.getToAccountNumber());
        assertEquals(1000L, result.getAmount());
        assertEquals("Test Memo", result.getMemo());
    }

    @Test
    void testProcessAutoTransfers() {
        when(autoTransferRepository.findAll()).thenReturn(Arrays.asList(autoTransfer));

        autoTransferService.processAutoTransfers();

        verify(transferService, times(1)).transfer("12345", "54321", 1000L, "Test Memo");
        verify(autoTransferRepository, times(1)).save(any(AutoTransfer.class));
    }

    @Test
    void testShouldProcessTransfer() {
        LocalDateTime now = LocalDateTime.now();
        autoTransfer.setScheduleStartDateTime(now.minusMinutes(1));

        boolean result = autoTransferService.shouldProcessTransfer(autoTransfer, now);

        assertTrue(result);
    }

    @Test
    void testUpdateNextSchedule() {
        autoTransferService.updateNextSchedule(autoTransfer);

        assertEquals(LocalDateTime.now().plusDays(1).getDayOfYear(), autoTransfer.getScheduleStartDateTime().getDayOfYear());
    }
}
