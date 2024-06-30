package ru.omon4412.minibank.middle.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.omon4412.minibank.middle.dto.CreateTransferRequestDto;
import ru.omon4412.minibank.middle.dto.TransferResponseDto;
import ru.omon4412.minibank.middle.service.TransferService;

@RestController
@RequiredArgsConstructor
public class TransferController {
    private final TransferService transferService;

    @PostMapping("/transfers")
    public TransferResponseDto transfer(@Valid @RequestBody CreateTransferRequestDto createTransferRequestDto) {
        return transferService.transfer(createTransferRequestDto);
    }
}
