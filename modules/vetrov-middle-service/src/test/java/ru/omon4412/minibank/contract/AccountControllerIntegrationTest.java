package ru.omon4412.minibank.contract;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.omon4412.minibank.BaseContextTest;
import ru.omon4412.minibank.middle.dto.NewAccountDto;
import ru.omon4412.minibank.middle.dto.ResponseAccountDto;
import ru.omon4412.minibank.middle.dto.UserRequestDto;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class AccountControllerIntegrationTest extends BaseContextTest {

    private static Long userId = 99L;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void registerUser() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setUserId(userId);
        userRequestDto.setUserName("TestUser");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_createAccount_withAccountName_shouldReturnNoContent() throws Exception {
        NewAccountDto newAccountDto = new NewAccountDto();
        newAccountDto.setAccountName("Test Account");

        mockMvc.perform(post("/users/{id}/accounts", userId++)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccountDto)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_createAccount_withoutAccountName_shouldReturnNoContent() throws Exception {
        NewAccountDto newAccountDto = new NewAccountDto();
        newAccountDto.setAccountName(null);

        mockMvc.perform(post("/users/{id}/accounts", userId++)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccountDto)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_getUsersAccounts_shouldReturnOneAccount() throws Exception {
        NewAccountDto newAccountDto = new NewAccountDto();
        newAccountDto.setAccountName("Test Account");

        mockMvc.perform(post("/users/{id}/accounts", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAccountDto)))
                .andExpect(status().isNoContent());

        String jsonResponse = mockMvc.perform(get("/users/{id}/accounts", userId++)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<ResponseAccountDto> accounts = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertThat(accounts).hasSize(1);

        ResponseAccountDto account = accounts.get(0);
        assertThat(account.getAccountName()).isEqualTo("Test Account");
    }
}