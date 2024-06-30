package ru.omon4412.minibank.middle.service;

import ru.omon4412.minibank.middle.dto.ResponseAccountDto;

public interface AccountServiceBalanceUpdate extends AccountService {
    void updateAccount(ResponseAccountDto updatedAccountDto);
}
