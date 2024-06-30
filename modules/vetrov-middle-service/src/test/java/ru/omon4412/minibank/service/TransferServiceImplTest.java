package ru.omon4412.minibank.service;

import feign.RetryableException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.omon4412.minibank.middle.client.BackendServiceClient;
import ru.omon4412.minibank.middle.dto.CreateTransferRequestDto;
import ru.omon4412.minibank.middle.dto.TransferResponseDto;
import ru.omon4412.minibank.middle.exception.NotFoundException;
import ru.omon4412.minibank.middle.service.TransferServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferServiceImplTest {
    @Mock
    private BackendServiceClient backendServiceClient;
    @InjectMocks
    private TransferServiceImpl transferService;

    @Test
    void test_transfer_success() {
        CreateTransferRequestDto createTransferRequestDto = new CreateTransferRequestDto();
        TransferResponseDto transferResponseDto = new TransferResponseDto();
        transferResponseDto.setTransferId("123-123");
        when(backendServiceClient.transferMoney(createTransferRequestDto))
                .thenReturn(ResponseEntity.ok(transferResponseDto));

        TransferResponseDto transfer = transferService.transfer(createTransferRequestDto);

        assertEquals("123-123", transfer.getTransferId());
    }

    @Test
    void test_transfer_userNotFound() {
        CreateTransferRequestDto createTransferRequestDto = new CreateTransferRequestDto();
        when(backendServiceClient.transferMoney(createTransferRequestDto))
                .thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> transferService.transfer(createTransferRequestDto));
    }

    @Test
    void test_transfer_whenServerIsDown() {
        CreateTransferRequestDto createTransferRequestDto = new CreateTransferRequestDto();
        when(backendServiceClient.transferMoney(createTransferRequestDto))
                .thenThrow(RetryableException.class);

        assertThrows(RetryableException.class, () -> transferService.transfer(createTransferRequestDto));
    }
}