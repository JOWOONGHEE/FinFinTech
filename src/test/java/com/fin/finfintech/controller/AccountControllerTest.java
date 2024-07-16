package com.fin.finfintech.controller;

import com.fin.finfintech.dto.AccountDto;
import com.fin.finfintech.dto.AccountInfo;
import com.fin.finfintech.dto.CreateAccount;
import com.fin.finfintech.dto.DeleteAccount;
import com.fin.finfintech.exception.AccountException;
import com.fin.finfintech.service.AccountService;
import com.fin.finfintech.security.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static com.fin.finfintech.type.ErrorCode.ACCOUNT_NOT_FOUND;
import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AccountController.class)
@WithMockUser(roles = "USER")
class AccountControllerTest {
    @MockBean
    private AccountService accountService;

    @MockBean
    private TokenProvider tokenProvider; // TokenProvider 모킹

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    @BeforeEach
    void setUp() {
        // TokenProvider가 null인지 확인
        if (tokenProvider == null) {
            System.out.println("TokenProvider is null");
        } else {
            // Token 생성 시도
            String generatedToken = tokenProvider.generateToken("test@test.com");
            if (generatedToken == null) {
                System.out.println("Generated token is null");
            } else {
                token = "Bearer " + generatedToken;
            }
        }
        System.out.println("Generated token: " + token);
    }



    @Test
    void successCreateAccount() throws Exception {
        //given
        given(accountService.createAccount(anyLong(), anyLong()))
                .willReturn(AccountDto.builder()
                        .userId(1L)
                        .accountNumber("1234567890")
                        .registeredAt(LocalDateTime.now())
                        .unRegisteredAt(LocalDateTime.now())
                        .build());
        //when
        //then
        mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token) // 토큰 추가
                        .content(objectMapper.writeValueAsString(
                                new CreateAccount.Request(333L, 100L)
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("1L"))
                .andExpect(jsonPath("$.accountNumber").value("1234567890"))
                .andDo(print());
    }

    @Test
    void successDeleteAccount() throws Exception {
        //given
        given(accountService.deleteAccount(anyLong(), anyString()))
                .willReturn(AccountDto.builder()
                        .userId(1L)
                        .accountNumber("1234567890")
                        .registeredAt(LocalDateTime.now())
                        .unRegisteredAt(LocalDateTime.now())
                        .build());
        //when
        //then
        mockMvc.perform(delete("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token) // 토큰 추가
                        .content(objectMapper.writeValueAsString(
                                new DeleteAccount.Request(333L, "1234567890")
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("1L"))
                .andExpect(jsonPath("$.accountNumber").value("1234567890"))
                .andDo(print());
    }

    @Test
    void successGetAccountsByUserId() throws Exception {
        //given
        given(accountService.getAccountsByUserId(anyLong()))
                .willReturn(List.of(AccountInfo.builder()
                                .balance(100L)
                                .accountNumber("1234567890")
                                .build(),
                        AccountInfo.builder()
                                .balance(1000L)
                                .accountNumber("1234567800")
                                .build(),
                        AccountInfo.builder()
                                .balance(10000L)
                                .accountNumber("1234567000")
                                .build()));

        //when
        //then
        mockMvc.perform(get("/account?user_id=1")
                        .header("Authorization", token)) // 토큰 추가
                        .andDo(print())
                        .andExpect(jsonPath("$[0].accountNumber").value("1234567890"))
                        .andExpect(jsonPath("$[0].balance").value("100"))
                        .andExpect(jsonPath("$[1].accountNumber").value("1234567800"))
                        .andExpect(jsonPath("$[1].balance").value("1000"))
                        .andExpect(jsonPath("$[2].accountNumber").value("1234567000"))
                        .andExpect(jsonPath("$[2].balance").value("10000"))
                        .andExpect(status().isOk());
    }

    @Test
    void failGetAccount() throws Exception {
        //given
        given(accountService.getAccountsByUserId(anyLong()))
                .willThrow(new AccountException(ACCOUNT_NOT_FOUND));

        //when
        //then
        mockMvc.perform(get("/account?user_id=1")
                        .header("Authorization", token)) // 토큰 추가
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.errorCode").value("ACCOUNT_NOT_FOUND"))
                        .andExpect(jsonPath("$.errorMessage").value(ACCOUNT_NOT_FOUND.getDescription()));
    }
}
