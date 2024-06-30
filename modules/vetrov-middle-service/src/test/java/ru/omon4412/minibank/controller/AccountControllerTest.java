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
import ru.omon4412.minibank.middle.controller.AccountController;
import ru.omon4412.minibank.middle.dto.NewAccountDto;
import ru.omon4412.minibank.middle.service.AccountService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {
    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController)
                .build();
    }

    @Test
    void test_registerUser_success() throws Exception {
        NewAccountDto newAccountDto = new NewAccountDto();
        newAccountDto.setAccountName("TestAccount");

        doNothing().when(accountService).createAccount(1L, newAccountDto);

        mockMvc.perform(post("/users/1/accounts").content(new ObjectMapper().writeValueAsString(newAccountDto))
                        .contentType("application/json"))
                .andExpect(status().isNoContent());

        verify(accountService, times(1)).createAccount(1L, newAccountDto);
    }

    @Test
    void testRegisterUser_ifAccountDtoIsNull_thenReturn400() throws Exception {
        mockMvc.perform(post("/users/1/accounts")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterUser_ifAccountNameIsNull_thenReturn400() throws Exception {
        NewAccountDto newAccountDto = new NewAccountDto();
        newAccountDto.setAccountName(null);
        mockMvc.perform(post("/users/1/accounts").content(new ObjectMapper().writeValueAsString(newAccountDto))
                        .contentType("application/json"))
                .andExpect(status().isNoContent());
    }
}