package com.fin.finfintech.service;

import com.fin.finfintech.domain.Account;
import com.fin.finfintech.domain.AutoTransfer;
import com.fin.finfintech.dto.AutoTransferDto;
import com.fin.finfintech.repository.AccountRepository;
import com.fin.finfintech.repository.AutoTransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AutoTransferService {
    @Autowired
    private TransferService transferService;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AutoTransferRepository autoTransferRepository;

    @Transactional
    public AutoTransferDto scheduleAutoTransfer(AutoTransferDto autoTransferDto) {
        Account fromAccount = accountRepository.findByAccountNumber(autoTransferDto.getFromAccountNumber())
                .orElseThrow(() -> new RuntimeException("출금 계좌를 찾을 수 없습니다."));

        Account toAccount = accountRepository.findByAccountNumber(autoTransferDto.getToAccountNumber())
                .orElseThrow(() -> new RuntimeException("입금 계좌를 찾을 수 없습니다."));

        AutoTransfer autoTransfer = AutoTransfer.builder()
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .amount(autoTransferDto.getAmount())
                .memo(autoTransferDto.getMemo())
                .scheduleStartDateTime(autoTransferDto.getScheduleStartDateTime())
                .frequency(autoTransferDto.getFrequency())
                .build();

        AutoTransfer savedTransfer = autoTransferRepository.save(autoTransfer);

        return convertToDto(savedTransfer);
    }

    private AutoTransferDto convertToDto(AutoTransfer autoTransfer) {
        return AutoTransferDto.builder()
                .fromAccountNumber(autoTransfer.getFromAccount().getAccountNumber())
                .toAccountNumber(autoTransfer.getToAccount().getAccountNumber())
                .amount(autoTransfer.getAmount())
                .memo(autoTransfer.getMemo())
                .transferDate(autoTransfer.getTransferDate())
                .scheduleStartDateTime(autoTransfer.getScheduleStartDateTime())
                .frequency(autoTransfer.getFrequency())
                .build();
    }

    @Scheduled(fixedRate = 60000) // 매 1분마다 실행
    public void processAutoTransfers() {
        LocalDateTime now = LocalDateTime.now();
        List<AutoTransfer> autoTransfers = autoTransferRepository.findAll();

        for (AutoTransfer transfer : autoTransfers) {
            if (shouldProcessTransfer(transfer, now)) {
                transferService.transfer(
                        transfer.getFromAccount().getAccountNumber(),
                        transfer.getToAccount().getAccountNumber(),
                        transfer.getAmount(),
                        transfer.getMemo()
                );

                updateNextSchedule(transfer);
                autoTransferRepository.save(transfer);
            }
        }
    }

    boolean shouldProcessTransfer(AutoTransfer transfer, LocalDateTime now) {
        return transfer.getScheduleStartDateTime().isBefore(now) || transfer.getScheduleStartDateTime().isEqual(now);
    }

    void updateNextSchedule(AutoTransfer transfer) {
        switch (transfer.getFrequency().toUpperCase()) {
            case "DAILY":
                transfer.setScheduleStartDateTime(transfer.getScheduleStartDateTime().plusDays(1));
                break;
            case "WEEKLY":
                transfer.setScheduleStartDateTime(transfer.getScheduleStartDateTime().plusWeeks(1));
                break;
            case "MONTHLY":
                transfer.setScheduleStartDateTime(transfer.getScheduleStartDateTime().plusMonths(1));
                break;
            default:
                throw new IllegalArgumentException("알 수 없는 빈도: " + transfer.getFrequency());
        }
    }
}
