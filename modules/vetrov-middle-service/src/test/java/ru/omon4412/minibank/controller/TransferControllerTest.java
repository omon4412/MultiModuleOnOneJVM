package ru.omon4412.minibank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.omon4412.minibank.middle.controller.TransferController;
import ru.omon4412.minibank.middle.dto.CreateTransferRequestDto;
import ru.omon4412.minibank.middle.dto.TransferResponseDto;
import ru.omon4412.minibank.middle.service.TransferService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TransferControllerTest {
    @Mock
    private TransferService transferService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private TransferController transferController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transferController)
                .build();
    }

    @Test
    void test_transferMoney_success() throws Exception {
        CreateTransferRequestDto createTransferRequestDto = new CreateTransferRequestDto();
        createTransferRequestDto.setFrom("user1");
        createTransferRequestDto.setTo("user2");
        createTransferRequestDto.setAmount(new BigDecimal(100));
        TransferResponseDto transferResponseDto = new TransferResponseDto();
        transferResponseDto.setTransferId("123-123");
        when(transferService.transfer(createTransferRequestDto)).thenReturn(transferResponseDto);

        String contentAsString = mockMvc.perform(post("/transfers")
                        .content(new ObjectMapper().writeValueAsString(createTransferRequestDto))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        TransferResponseDto transferResponseDto1 = objectMapper.readValue(contentAsString, TransferResponseDto.class);
        assertEquals(transferResponseDto.getTransferId(), transferResponseDto1.getTransferId());
    }

    @Test
    void test_transferMoney_ifBodyIsEmpty_thenReturn400() throws Exception {
        mockMvc.perform(post("/transfers")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void test_transferMoney_ifToIsNull_thenReturn400() throws Exception {
        CreateTransferRequestDto createTransferRequestDto = new CreateTransferRequestDto();
        createTransferRequestDto.setFrom("user1");
        createTransferRequestDto.setTo(null);
        createTransferRequestDto.setAmount(new BigDecimal(100));
        mockMvc.perform(post("/transfers").content(new ObjectMapper().writeValueAsString(createTransferRequestDto))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void test_transferMoney_ifFromIsNull_thenReturn400() throws Exception {
        CreateTransferRequestDto createTransferRequestDto = new CreateTransferRequestDto();
        createTransferRequestDto.setFrom(null);
        createTransferRequestDto.setTo("user2");
        createTransferRequestDto.setAmount(new BigDecimal(100));
        mockMvc.perform(post("/transfers").content(new ObjectMapper().writeValueAsString(createTransferRequestDto))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }
}