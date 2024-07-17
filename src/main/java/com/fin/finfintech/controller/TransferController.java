package com.fin.finfintech.controller;

import com.fin.finfintech.dto.TransferDto;
import com.fin.finfintech.service.TransferService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfer")
@AllArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<String> transfer(@RequestBody TransferDto transferRequest) {
        transferService.transfer(transferRequest.getFromAccountNumber(), transferRequest.getToAccountNumber(), transferRequest.getAmount());
        return ResponseEntity.ok("이체가 성공적으로 완료되었습니다.");
    }
}
