package com.fin.finfintech.controller;

import com.fin.finfintech.aop.AccountLock;
import com.fin.finfintech.dto.*;
import com.fin.finfintech.exception.AccountException;
import com.fin.finfintech.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 잔액 관련 컨트롤러
 * 1. 잔액 사용
 * 2. 잔액 사용 취소
 * 3. 거래 확인
 * 4. 입금
 * 5. 출금
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    /**
     * 잔액 사용
     * @param request
     * @return
     */
    @PostMapping("/transaction/use")
    @AccountLock
    public UseBalance.Response useBalance(
            @RequestBody @Valid UseBalance.Request request
    ) throws InterruptedException {
        try {
            //AOP LOCK이 잘 작동하는 지 확인하기 위해 넣음
            Thread.sleep(3000L);

            return UseBalance.Response.from(transactionService.useBalance(
                            request.getUserId()
                            , request.getAccountNumber()
                            , request.getAmount()
                    )
            );
        } catch (AccountException ae) {
            log.error("Failed to use balance");
            transactionService.saveFailedUseTransaction(
                    request.getAccountNumber(),
                    request.getAmount()
            );

            throw ae;
        }
    }

    @PostMapping("/transaction/cancel")
    @AccountLock
    public CancelBalance.Response cancelBalance(
            @RequestBody @Valid CancelBalance.Request request
    ) {
        try {
            return CancelBalance.Response.from(transactionService.cancelBalance(
                    request.getTransactionId()
                    , request.getAccountNumber()
                    , request.getAmount()
            ));
        } catch (AccountException ae) {
            log.error("Failed to cancel used balance");
            transactionService.saveFailedCancelTransaction(
                    request.getAccountNumber(),
                    request.getAmount()
            );
            throw ae;
        }
    }

    @GetMapping("/transaction")
    public QueryTransactionResponse queryTransaction(
            @RequestParam("transactionId") String transactionId
    ) {
        return QueryTransactionResponse.from(
                transactionService.queryTransaction(transactionId)
        );
    }

    @PostMapping("/transaction/deposit")
    public Withdraw.Response deposit(
            @RequestBody @Valid Deposit.Request request
    ) {
        TransactionDto transactionDto = transactionService.deposit(
                request.getUserId(),
                request.getAccountNumber(),
                request.getAmount()
        );

        return Deposit.Response.from(
                transactionDto.getAccountNumber(),
                transactionDto.getAmount(),
                transactionDto.getBalanceSnapshot(),
                transactionDto.getTransactionId(),
                transactionDto.getTransactedAt()
        );
    }



    @PostMapping("/transaction/withdraw")
    public Withdraw.Response withdraw(
            @RequestBody @Valid Withdraw.Request request
    ) {
        TransactionDto transactionDto = transactionService.withdraw(
                request.getUserId(),
                request.getAccountNumber(),
                request.getAmount()
        );

        return Withdraw.Response.from(
                transactionDto.getAccountNumber(),
                transactionDto.getAmount(),
                transactionDto.getBalanceSnapshot(),
                transactionDto.getTransactionId(),
                transactionDto.getTransactedAt()
                );
    }
}
