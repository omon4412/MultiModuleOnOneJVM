package ru.omon4412.minibank.telegrambot.service;

import ru.omon4412.minibank.telegrambot.dto.NewAccountDto;
import ru.omon4412.minibank.telegrambot.dto.ResponseAccountDto;
import ru.omon4412.minibank.telegrambot.util.Result;

import java.util.Collection;

public interface AccountService {
    Result<String> createAccount(NewAccountDto newAccountDto, Long userId);

    Result<Collection<ResponseAccountDto>> getUserAccounts(Long userId);
}
