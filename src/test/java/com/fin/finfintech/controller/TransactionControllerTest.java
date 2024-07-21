package com.fin.finfintech.controller;

import com.fin.finfintech.dto.*;
import com.fin.finfintech.security.TokenProvider;
import com.fin.finfintech.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fin.finfintech.type.TransactionResultType;
import com.fin.finfintech.type.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
@WithMockUser(roles = "USER")
class TransactionControllerTest {

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TokenProvider tokenProvider; // TokenProvider 모킹

    private String token;

    @BeforeEach
    void setUp() {
        // TokenProvider의 동작을 모킹
        given(tokenProvider.generateToken(anyString())).willReturn("mocked-token");

        // Token 생성 시도
        token = "Bearer " + tokenProvider.generateToken("test@test.com");

        System.out.println("Generated token: " + token);
    }


    @Test
    void useBalance_success() throws Exception {
        // given
        given(transactionService.useBalance(anyLong(), anyString(), anyLong()))
                .willReturn(TransactionDto.builder()
                        .accountNumber("1234567890")
                        .transactionType(TransactionType.USE)
                        .transactionResultType(TransactionResultType.S)
                        .amount(10000L)
                        .balanceSnapshot(1000L)
                        .transactionId(UUID.randomUUID().toString().replace("-", ""))
                        .transactedAt(LocalDateTime.now())
                        .build());

        // when & then
        mockMvc.perform(post("/transaction/use")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(
                                new UseBalance.Request(333L, "1234567890", 1000L)
                        )))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("1234567890"))
                .andExpect(jsonPath("$.transactionResult").value("S"))
                .andExpect(jsonPath("$.amount").value(10000L));
    }

    @Test
    void cancelBalance_success() throws Exception {
        // given
        given(transactionService.cancelBalance(anyString(), anyString(), anyLong()))
                .willReturn(TransactionDto.builder()
                        .accountNumber("1234567890")
                        .transactionId("transactionId")
                        .transactedAt(LocalDateTime.now())
                        .amount(1000L)
                        .transactionResultType(TransactionResultType.S)
                        .build());

        // when & then
        mockMvc.perform(post("/transaction/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(
                                new CancelBalance.Request("transaction", "1234567800", 1000L)
                        )))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(1000L))
                .andExpect(jsonPath("$.accountNumber").value("1234567890"));
    }

    @Test
    void queryTransaction_success() throws Exception {
        // given
        given(transactionService.queryTransaction(anyString()))
                .willReturn(TransactionDto.builder()
                        .transactionId("transactionId")
                        .transactionType(TransactionType.USE)
                        .transactionResultType(TransactionResultType.S)
                        .accountNumber("1234567890")
                        .transactedAt(LocalDateTime.now())
                        .amount(1000L)
                        .build());

        // when & then
        mockMvc.perform(get("/transaction")
                        .param("transactionId", "transactionId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value("transactionId"))
                .andExpect(jsonPath("$.transactionType").value("USE"))
                .andExpect(jsonPath("$.transactionResult").value("S"))
                .andExpect(jsonPath("$.accountNumber").value("1234567890"))
                .andExpect(jsonPath("$.amount").value(1000L));
    }

    @Test
    void deposit_success() throws Exception {
        // given
        given(transactionService.deposit(anyLong(), anyString(), anyLong()))
                .willReturn(TransactionDto.builder()
                        .accountNumber("1234567890")
                        .transactionType(TransactionType.DEPOSIT)
                        .transactionResultType(TransactionResultType.S)
                        .amount(10000L)
                        .balanceSnapshot(50000L)
                        .transactionId(UUID.randomUUID().toString().replace("-", ""))
                        .transactedAt(LocalDateTime.now())
                        .build());

        // when & then
        mockMvc.perform(post("/transaction/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(
                                new Deposit.Request(333L, "1234567890", 10000L)
                        )))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("1234567890"))
                .andExpect(jsonPath("$.amount").value(10000L))
                .andExpect(jsonPath("$.balanceSnapshot").value(50000L));
    }

    @Test
    void withdraw_success() throws Exception {
        // given
        given(transactionService.withdraw(anyLong(), anyString(), anyLong()))
                .willReturn(TransactionDto.builder()
                        .accountNumber("1234567890")
                        .transactionType(TransactionType.WITHDRAW)
                        .transactionResultType(TransactionResultType.S)
                        .amount(5000L)
                        .balanceSnapshot(45000L)
                        .transactionId(UUID.randomUUID().toString().replace("-", ""))
                        .transactedAt(LocalDateTime.now())
                        .build());

        // when & then
        mockMvc.perform(post("/transaction/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(
                                new Withdraw.Request(333L, "1234567890", 5000L)
                        )))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("1234567890"))
                .andExpect(jsonPath("$.amount").value(5000L))
                .andExpect(jsonPath("$.balanceSnapshot").value(45000L));
    }
}
