package com.fin.finfintech.controller;

import com.fin.finfintech.dto.TransferDto;
import com.fin.finfintech.service.TransferService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class TransferControllerTest {

    @Mock
    private TransferService transferService;

    @InjectMocks
    private TransferController transferController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transferController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testTransfer_Success() throws Exception {
        TransferDto request = new TransferDto();
        request.setFromAccountNumber("1234567890");
        request.setToAccountNumber("0987654321");
        request.setAmount(1000L);
        request.setMemo("Test Transfer");

        TransferDto response = new TransferDto();
        response.setFromAccountNumber("1234567890");
        response.setToAccountNumber("0987654321");
        response.setAmount(1000L);
        response.setMemo("Test Transfer");

        when(transferService.transfer(any(String.class), any(String.class), any(Long.class), any(String.class)))
                .thenReturn(response);

        mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fromAccountNumber").value("1234567890"))
                .andExpect(jsonPath("$.toAccountNumber").value("0987654321"))
                .andExpect(jsonPath("$.amount").value(1000.0))
                .andExpect(jsonPath("$.memo").value("Test Transfer"));
    }

}
