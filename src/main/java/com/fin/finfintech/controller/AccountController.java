package com.fin.finfintech.controller;

import com.fin.finfintech.domain.Account;
import com.fin.finfintech.domain.AccountUser;
import com.fin.finfintech.domain.User;
import com.fin.finfintech.dto.AccountDto;
import com.fin.finfintech.dto.AccountInfo;
import com.fin.finfintech.dto.CreateAccount;
import com.fin.finfintech.dto.DeleteAccount;
import com.fin.finfintech.service.AccountService;
import com.fin.finfintech.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final UserService userService;
    private final AccountService accountService;

    @PostMapping("/account")
    public CreateAccount.Response createAccount(
            @RequestBody @Valid CreateAccount.Request request
    ) {
        return CreateAccount.Response.from(
                accountService.createAccount(
                        request.getUserId()
                        , request.getInitialBalance()
                )
        );
    }

    @DeleteMapping("/account")
    public DeleteAccount.Response deleteAccount(
            @RequestBody @Valid DeleteAccount.Request request
    ) {
        return DeleteAccount.Response.from(
                accountService.deleteAccount(
                        request.getUserId()
                        , request.getAccountNumber()
                )
        );
    }

    @GetMapping("/account")
    public List<AccountInfo> getAccountsByUserId(
            @RequestParam("user_id") Long userId
    ) {
        return accountService.getAccountsByUserId(userId);
    }

}
