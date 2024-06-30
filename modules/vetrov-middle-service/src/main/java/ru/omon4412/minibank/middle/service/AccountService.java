package ru.omon4412.minibank.middle.service;

import ru.omon4412.minibank.middle.dto.NewAccountDto;
import ru.omon4412.minibank.middle.dto.ResponseAccountDto;

import java.util.Collection;

public interface AccountService {

    void createAccount(Long userId, NewAccountDto newAccountDto);

    Collection<ResponseAccountDto> getUserAccounts(Long userId);
}
