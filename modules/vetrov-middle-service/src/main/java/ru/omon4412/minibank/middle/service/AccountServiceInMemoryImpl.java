package ru.omon4412.minibank.middle.service;

import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.omon4412.minibank.middle.dto.NewAccountDto;
import ru.omon4412.minibank.middle.dto.ResponseAccountDto;
import ru.omon4412.minibank.middle.exception.ConflictException;
import ru.omon4412.minibank.middle.exception.NotFoundException;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@ConditionalOnProperty(value = "application.services.type", havingValue = "inMemory")
public class AccountServiceInMemoryImpl implements AccountServiceBalanceUpdate {
    private final Map<Long, Account> accounts = new ConcurrentHashMap<>();
    private final RegistrationService registrationService;

    public AccountServiceInMemoryImpl(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Override
    public void createAccount(Long userId, NewAccountDto newAccountDto) {
        if (registrationService.getUsernameById(userId) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (accounts.containsKey(userId)) {
            throw new ConflictException("У пользователя уже есть счет");
        }
        String uuid = UUID.randomUUID().toString();
        Account account = new Account();
        account.setAccountId(uuid);
        account.setOwnerId(userId);
        account.setAccountName(newAccountDto.getAccountName() == null ? "Акционный" : newAccountDto.getAccountName());
        account.setAmount(new BigDecimal(5000));
        accounts.put(userId, account);
    }

    @Override
    public Collection<ResponseAccountDto> getUserAccounts(Long userId) {
        if (registrationService.getUsernameById(userId) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        Collection<ResponseAccountDto> accountDtos = new ArrayList<>();
        if (!accounts.containsKey(userId)) {
            return Collections.emptyList();
        }
        accountDtos.add(Account.toResponseAccountDto(accounts.get(userId)));
        return accountDtos;
    }

    @Override
    public void updateAccount(ResponseAccountDto updatedAccountDto) {
        Account account = accounts.values().stream()
                .filter(acc -> acc.accountId.equals(updatedAccountDto.getAccountId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Аккаунт не найден"));

        account.setAccountName(updatedAccountDto.getAccountName());
        account.setAmount(updatedAccountDto.getAmount());
        accounts.put(account.ownerId, account);
    }

    @Setter
    static class Account {
        private String accountId;
        private String accountName;
        private BigDecimal amount;
        private Long ownerId;

        public static ResponseAccountDto toResponseAccountDto(Account account) {
            return ResponseAccountDto.builder()
                    .accountId(account.accountId)
                    .accountName(account.accountName)
                    .amount(account.amount)
                    .build();
        }
    }
}
