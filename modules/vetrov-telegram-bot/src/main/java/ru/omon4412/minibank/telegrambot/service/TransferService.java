package ru.omon4412.minibank.telegrambot.service;

import ru.omon4412.minibank.telegrambot.dto.CreateTransferRequestDto;
import ru.omon4412.minibank.telegrambot.dto.TransferResponseDto;
import ru.omon4412.minibank.telegrambot.util.Result;

public interface TransferService {
    Result<TransferResponseDto> transfer(CreateTransferRequestDto createTransferRequestDto);
}
