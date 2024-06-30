package ru.omon4412.minibank.service;

import feign.RetryableException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.omon4412.minibank.middle.client.BackendServiceClient;
import ru.omon4412.minibank.middle.dto.NewAccountDto;
import ru.omon4412.minibank.middle.dto.ResponseAccountDto;
import ru.omon4412.minibank.middle.exception.ConflictException;
import ru.omon4412.minibank.middle.exception.NotFoundException;
import ru.omon4412.minibank.middle.service.AccountServiceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {
    @Mock
    private BackendServiceClient backendServiceClient;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void test_createAccount_success() {
        NewAccountDto newAccountDto = new NewAccountDto();
        newAccountDto.setAccountName("test");
        when(backendServiceClient.createAccount(newAccountDto, 1L)).thenReturn(ResponseEntity.noContent().build());
        accountService.createAccount(1L, newAccountDto);
    }

    @Test
    void test_createAccount_whenAccountAlreadyExists() {
        NewAccountDto newAccountDto = new NewAccountDto();
        newAccountDto.setAccountName("test");
        ConflictException feignClientException = new ConflictException("");
        when(backendServiceClient.createAccount(newAccountDto, 1L)).thenThrow(feignClientException);
        assertThrows(ConflictException.class, () -> accountService.createAccount(1L, newAccountDto));
    }

    @Test
    void test_createAccount_whenServerIsDown() {
        NewAccountDto newAccountDto = new NewAccountDto();
        newAccountDto.setAccountName("test");
        when(backendServiceClient.createAccount(newAccountDto, 1L)).thenThrow(RetryableException.class);
        assertThrows(RetryableException.class, () -> accountService.createAccount(1L, newAccountDto));
    }

    @Test
    void test_getOneUsersAccount() {
        Collection<ResponseAccountDto> responseAccountDtos = new ArrayList<>();
        ResponseAccountDto responseAccountDto1 = ResponseAccountDto.builder()
                .accountName("TestName1")
                .amount(new BigDecimal(5000))
                .accountId("TestId1")
                .build();
        responseAccountDtos.add(responseAccountDto1);
        when(backendServiceClient.getUsersAccounts(1L)).thenReturn(ResponseEntity.ok(responseAccountDtos));

        Collection<ResponseAccountDto> userAccounts = accountService.getUserAccounts(1L);

        assertEquals(responseAccountDtos.size(), userAccounts.size());
        assertEquals(responseAccountDtos, userAccounts);
    }

    @Test
    void test_getTwoUsersAccount() {
        Collection<ResponseAccountDto> responseAccountDtos = new ArrayList<>();
        ResponseAccountDto responseAccountDto1 = ResponseAccountDto.builder()
                .accountName("TestName1")
                .amount(new BigDecimal(5000))
                .accountId("TestId1")
                .build();
        responseAccountDtos.add(responseAccountDto1);
        ResponseAccountDto responseAccountDto2 = ResponseAccountDto.builder()
                .accountName("TestName2")
                .amount(new BigDecimal(5000))
                .accountId("TestId2")
                .build();
        responseAccountDtos.add(responseAccountDto2);
        when(backendServiceClient.getUsersAccounts(1L)).thenReturn(ResponseEntity.ok(responseAccountDtos));

        Collection<ResponseAccountDto> userAccounts = accountService.getUserAccounts(1L);

        assertEquals(responseAccountDtos.size(), userAccounts.size());
        assertEquals(responseAccountDtos, userAccounts);
    }

    @Test
    void test_getZeroUsersAccount() {
        when(backendServiceClient.getUsersAccounts(1L)).thenReturn(ResponseEntity.ok(Collections.emptyList()));

        Collection<ResponseAccountDto> userAccounts = accountService.getUserAccounts(1L);

        assertEquals(0, userAccounts.size());
    }

    @Test
    void test_getUsersAccounts_whenServerIsDown() {
        when(backendServiceClient.getUsersAccounts(1L)).thenThrow(RetryableException.class);

        assertThrows(RetryableException.class, () -> accountService.getUserAccounts(1L));
    }

    @Test
    void test_getUsersAccounts_whenUserNotFound() {
        NotFoundException feignClientException = new NotFoundException("");
        when(backendServiceClient.getUsersAccounts(1L)).thenThrow(feignClientException);

        assertThrows(NotFoundException.class, () -> accountService.getUserAccounts(1L));
    }
}