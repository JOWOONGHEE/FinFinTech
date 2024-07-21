package com.fin.finfintech.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fin.finfintech.dto.AutoTransferDto;
import com.fin.finfintech.security.TokenProvider;
import com.fin.finfintech.service.AutoTransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AutoTransferController.class)
@WithMockUser(roles = "USER")
class AutoTransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AutoTransferService autoTransferService;

    @Autowired
    private ObjectMapper objectMapper;

    private AutoTransferDto autoTransferDto;

    @BeforeEach
    void setUp() {
        autoTransferDto = AutoTransferDto.builder()
                .fromAccountNumber("12345")
                .toAccountNumber("54321")
                .amount(1000L)
                .memo("Test Memo")
                .scheduleStartDateTime(LocalDateTime.now())
                .frequency("DAILY")
                .build();
    }

    @Test
    void testScheduleAutoTransfer() throws Exception {
        // Mocking the service method
        Mockito.when(autoTransferService.scheduleAutoTransfer(any(AutoTransferDto.class)))
                .thenReturn(autoTransferDto);

        // Converting the DTO to JSON
        String jsonRequest = objectMapper.writeValueAsString(autoTransferDto);

        mockMvc.perform(post("/autotransfer")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonRequest));
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public TokenProvider tokenProvider() {
            return Mockito.mock(TokenProvider.class);
        }
    }
}
