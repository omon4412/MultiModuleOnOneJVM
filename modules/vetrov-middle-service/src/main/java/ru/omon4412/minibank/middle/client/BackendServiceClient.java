package ru.omon4412.minibank.middle.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.omon4412.minibank.middle.dto.*;

import java.util.Collection;

@FeignClient(name = "backendService", url = "${application.backendService.url}")
public interface BackendServiceClient {

    @PostMapping("/v2/users")
    ResponseEntity<Void> registerUser(UserRequestDto userRequestDto);

    @PostMapping("/v2/users/{id}/accounts")
    ResponseEntity<Void> createAccount(NewAccountDto newAccountDto, @PathVariable("id") Long userId);

    @GetMapping("/v2/users/{id}/accounts")
    ResponseEntity<Collection<ResponseAccountDto>> getUsersAccounts(@PathVariable("id") Long userId);

    @GetMapping("v2/users/{id:\\d+}")
    ResponseEntity<UsernameResponseDto> getUsernameById(@PathVariable("id") Long userId);

    @GetMapping("/v2/users/{username:[a-zA-Z]+}")
    ResponseEntity<UserIdResponseDto> getUserIdByUserName(@PathVariable("username") String username);

    @PostMapping("/v2/transfers")
    ResponseEntity<TransferResponseDto> transferMoney(CreateTransferRequestDto createTransferRequestDto);
}
