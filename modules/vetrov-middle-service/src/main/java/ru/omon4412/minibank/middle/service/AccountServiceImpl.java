package ru.omon4412.minibank.middle.service;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.omon4412.minibank.middle.client.BackendServiceClient;
import ru.omon4412.minibank.middle.dto.NewAccountDto;
import ru.omon4412.minibank.middle.dto.ResponseAccountDto;

import java.util.Collection;

@Service
@ConditionalOnProperty(value = "application.services.type", havingValue = "backend")
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final BackendServiceClient backendServiceClient;

    @Override
    public void createAccount(Long userId, NewAccountDto newAccountDto) {
        newAccountDto.setAccountName(newAccountDto.getAccountName() == null ? "Акционный"
                : newAccountDto.getAccountName());
        backendServiceClient.createAccount(newAccountDto, userId);
    }

    @Override
    public Collection<ResponseAccountDto> getUserAccounts(Long userId) {
        return backendServiceClient.getUsersAccounts(userId).getBody();
    }
}
