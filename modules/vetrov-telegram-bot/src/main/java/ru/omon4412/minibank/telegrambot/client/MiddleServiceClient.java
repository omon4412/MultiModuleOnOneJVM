package ru.omon4412.minibank.telegrambot.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.omon4412.minibank.telegrambot.configuration.CustomFeignClientConfiguration;
import ru.omon4412.minibank.telegrambot.dto.*;

import java.util.Collection;

@FeignClient(name = "middleService", url = "${application.middleService.url}", configuration = CustomFeignClientConfiguration.class)
public interface MiddleServiceClient {

    @PostMapping("/users")
    ResponseEntity<Void> registerUser(UserRequestDto userRequestDto);

    @PostMapping("/users/{id}/accounts")
    ResponseEntity<Void> createAccount(NewAccountDto newAccountDto, @PathVariable("id") Long userId);

    @GetMapping("/users/{id}/accounts")
    ResponseEntity<Collection<ResponseAccountDto>> getUserAccounts(@PathVariable("id") Long userId);

    @PostMapping("/transfers")
    ResponseEntity<TransferResponseDto> transfer(CreateTransferRequestDto createTransferRequestDto);

    @GetMapping("/users/{id}")
    ResponseEntity<UserIdResponseDto> getUserIdByUserName(@PathVariable("id") String username);
}
