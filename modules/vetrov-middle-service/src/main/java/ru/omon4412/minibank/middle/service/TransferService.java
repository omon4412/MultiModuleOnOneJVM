package ru.omon4412.minibank.middle.service;

import ru.omon4412.minibank.middle.dto.CreateTransferRequestDto;
import ru.omon4412.minibank.middle.dto.TransferResponseDto;

public interface TransferService {

    TransferResponseDto transfer(CreateTransferRequestDto createTransferRequestDto);
}
