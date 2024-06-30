package ru.omon4412.minibank.middle.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.omon4412.minibank.middle.dto.CreateTransferRequestDto;
import ru.omon4412.minibank.middle.dto.ResponseAccountDto;
import ru.omon4412.minibank.middle.dto.TransferResponseDto;
import ru.omon4412.minibank.middle.dto.UserIdResponseDto;
import ru.omon4412.minibank.middle.exception.ConflictException;
import ru.omon4412.minibank.middle.exception.NotFoundException;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(value = "application.services.type", havingValue = "inMemory")
public class TransferServiceInMemoryImpl implements TransferService {
    private final Map<String, CreateTransferRequestDto> transferResponseDtoMap = new ConcurrentHashMap<>();
    private final RegistrationService registrationService;
    private final AccountServiceBalanceUpdate accountService;

    @Override
    public TransferResponseDto transfer(CreateTransferRequestDto createTransferRequestDto) {
        if (registrationService.getUserIdByUsername(createTransferRequestDto.getFrom()) == null) {
            throw new NotFoundException("Сначала нужно зарегистрироваться");
        }
        UserIdResponseDto fromUserId = registrationService.getUserIdByUsername(createTransferRequestDto.getFrom());
        ResponseAccountDto accountFrom = accountService.getUserAccounts(fromUserId.getUserId())
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("У вас нет счетов"));
        if (registrationService.getUserIdByUsername(createTransferRequestDto.getTo()) == null) {
            throw new NotFoundException("Конечный пользователь не найден");
        }
        if (createTransferRequestDto.getFrom().equals(createTransferRequestDto.getTo())) {
            throw new ConflictException("Нельзя перевести средства самому себе");
        }
        if (createTransferRequestDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ConflictException("Нельзя перевести отрицательное количество средств");
        }

        UserIdResponseDto toUserId = registrationService.getUserIdByUsername(createTransferRequestDto.getTo());

        ResponseAccountDto accountTo = accountService.getUserAccounts(toUserId.getUserId())
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("У конечного пользователя нет счетов"));

        if (accountFrom.getAmount().compareTo(createTransferRequestDto.getAmount()) < 0) {
            throw new ConflictException("Недостаточно средств");
        }
        accountFrom.setAmount(accountFrom.getAmount().subtract(createTransferRequestDto.getAmount()));
        accountTo.setAmount(accountTo.getAmount().add(createTransferRequestDto.getAmount()));

        accountService.updateAccount(accountFrom);
        accountService.updateAccount(accountTo);

        String transferUUID = UUID.randomUUID().toString();
        transferResponseDtoMap.put(transferUUID, createTransferRequestDto);
        return new TransferResponseDto(transferUUID);
    }
}
