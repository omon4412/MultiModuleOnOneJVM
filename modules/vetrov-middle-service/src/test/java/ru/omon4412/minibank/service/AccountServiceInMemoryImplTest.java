package ru.omon4412.minibank.service;

import org.junit.jupiter.api.Test;
import ru.omon4412.minibank.middle.dto.NewAccountDto;
import ru.omon4412.minibank.middle.dto.ResponseAccountDto;
import ru.omon4412.minibank.middle.dto.UserRequestDto;
import ru.omon4412.minibank.middle.exception.ConflictException;
import ru.omon4412.minibank.middle.exception.NotFoundException;
import ru.omon4412.minibank.middle.service.AccountServiceInMemoryImpl;
import ru.omon4412.minibank.middle.service.RegistrationServiceInMemoryImpl;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountServiceInMemoryImplTest {
    private final RegistrationServiceInMemoryImpl registrationServiceInMemory = new RegistrationServiceInMemoryImpl();
    private final AccountServiceInMemoryImpl accountServiceInMemory = new AccountServiceInMemoryImpl(registrationServiceInMemory);


    @Test
    void test_createAccount_whenUserNotRegistered() {
        assertThrows(NotFoundException.class, () -> accountServiceInMemory.createAccount(101010L, null));
    }

    @Test
    void test_createAccount_success() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setUserId(1L);
        userRequestDto.setUserName("user1");
        NewAccountDto newAccountDto = new NewAccountDto();
        newAccountDto.setAccountName("account1");

        registrationServiceInMemory.registerUser(userRequestDto);
        accountServiceInMemory.createAccount(1L, newAccountDto);
    }

    @Test
    void test_createAccount_whenUserAlreadyHasAccount() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setUserId(1L);
        userRequestDto.setUserName("user1");
        NewAccountDto newAccountDto1 = new NewAccountDto();
        newAccountDto1.setAccountName("account1");
        NewAccountDto newAccountDto2 = new NewAccountDto();
        newAccountDto2.setAccountName("account2");

        registrationServiceInMemory.registerUser(userRequestDto);
        accountServiceInMemory.createAccount(1L, newAccountDto1);

        assertThrows(ConflictException.class, () -> accountServiceInMemory.createAccount(1L, newAccountDto2));
    }

    @Test
    void test_getOneUsersAccount() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setUserId(1L);
        userRequestDto.setUserName("user1");
        NewAccountDto newAccountDto1 = new NewAccountDto();
        newAccountDto1.setAccountName("account1");

        registrationServiceInMemory.registerUser(userRequestDto);
        accountServiceInMemory.createAccount(1L, newAccountDto1);
        Collection<ResponseAccountDto> userAccounts = accountServiceInMemory.getUserAccounts(1L);

        assertEquals(1, userAccounts.size());
    }

    @Test
    void test_getZeroUsersAccount() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setUserId(1L);
        userRequestDto.setUserName("user1");

        registrationServiceInMemory.registerUser(userRequestDto);

        Collection<ResponseAccountDto> userAccounts = accountServiceInMemory.getUserAccounts(1L);

        assertEquals(0, userAccounts.size());
    }
}