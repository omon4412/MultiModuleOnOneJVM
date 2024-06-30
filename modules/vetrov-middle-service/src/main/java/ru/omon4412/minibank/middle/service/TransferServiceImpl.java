package ru.omon4412.minibank.middle.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.omon4412.minibank.middle.client.BackendServiceClient;
import ru.omon4412.minibank.middle.dto.CreateTransferRequestDto;
import ru.omon4412.minibank.middle.dto.TransferResponseDto;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(value = "application.services.type", havingValue = "backend")
public class TransferServiceImpl implements TransferService {
    private final BackendServiceClient backendServiceClient;

    @Override
    public TransferResponseDto transfer(CreateTransferRequestDto createTransferRequestDto) {
        return backendServiceClient.transferMoney(createTransferRequestDto).getBody();
    }
}
