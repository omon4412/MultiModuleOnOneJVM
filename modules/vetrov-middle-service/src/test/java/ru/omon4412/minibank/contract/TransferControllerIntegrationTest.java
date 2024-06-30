package ru.omon4412.minibank.contract;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.omon4412.minibank.BaseContextTest;
import ru.omon4412.minibank.middle.dto.CreateTransferRequestDto;
import ru.omon4412.minibank.middle.dto.NewAccountDto;
import ru.omon4412.minibank.middle.dto.UserRequestDto;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class TransferControllerIntegrationTest extends BaseContextTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static Long userId = 9999L;

    @BeforeEach
    public void registerUserAndCreateAccount() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setUserId(userId);
        userRequestDto.setUserName("TestUser" + userId);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isNoContent());

        NewAccountDto newAccountDto = new NewAccountDto();
        newAccountDto.setAccountName("Test Account" + userId);

        mockMvc.perform(post("/users/{id}/accounts", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccountDto)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_transferMoney_success() throws Exception {
        CreateTransferRequestDto createTransferRequestDto = new CreateTransferRequestDto();
        createTransferRequestDto.setFrom("TestUser" + userId);

        UserRequestDto userRequestDto2 = new UserRequestDto();
        userRequestDto2.setUserId(++userId);
        userRequestDto2.setUserName("TestUser" + userId);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto2)))
                .andExpect(status().isNoContent());

        NewAccountDto newAccountDto2 = new NewAccountDto();
        newAccountDto2.setAccountName("Test Account" + userId);

        mockMvc.perform(post("/users/{id}/accounts", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccountDto2)))
                .andExpect(status().isNoContent());

        createTransferRequestDto.setTo("TestUser" + userId++);
        createTransferRequestDto.setAmount(new BigDecimal(100));

        mockMvc.perform(post("/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTransferRequestDto)))
                .andExpect(status().isOk());
    }
}
