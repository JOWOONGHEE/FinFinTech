package com.fin.finfintech.controller;

import com.fin.finfintech.dto.TransferDto;
import com.fin.finfintech.service.TransactionService;
import com.fin.finfintech.service.TransferService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfer")
@AllArgsConstructor
public class TransferController {

    @Autowired
    private TransferService transferService;

    @PostMapping
    public TransferDto transfer(@RequestBody @Valid TransferDto request) {
        TransferDto transferDto = transferService.transfer(
                request.getFromAccountNumber(),
                request.getToAccountNumber(),
                request.getAmount(),
                request.getMemo()
        );

        return transferDto;
    }
}
