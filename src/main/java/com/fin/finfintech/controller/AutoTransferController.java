package com.fin.finfintech.controller;

import com.fin.finfintech.dto.AutoTransferDto;
import com.fin.finfintech.service.AutoTransferService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/autotransfer")
@AllArgsConstructor
public class AutoTransferController {

    @Autowired
    private AutoTransferService autoTransferService;

    @PostMapping
    public AutoTransferDto scheduleAutoTransfer(@RequestBody @Valid AutoTransferDto request) {
        AutoTransferDto autoTransferDto = autoTransferService.scheduleAutoTransfer(request);
        return autoTransferDto;
    }
}
