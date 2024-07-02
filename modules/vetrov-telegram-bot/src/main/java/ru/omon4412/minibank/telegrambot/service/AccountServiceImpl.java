package ru.omon4412.minibank.telegrambot.service;

import feign.FeignException;
import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.omon4412.minibank.telegrambot.client.MiddleServiceClient;
import ru.omon4412.minibank.telegrambot.dto.NewAccountDto;
import ru.omon4412.minibank.telegrambot.dto.ResponseAccountDto;
import ru.omon4412.minibank.telegrambot.util.Result;

import java.util.Collection;

@Service("middleAccountServiceImpl")
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final MiddleServiceClient middleServiceClient;

    @Override
    public Result<String> createAccount(NewAccountDto newAccountDto, Long userId) {
        try {
            middleServiceClient.createAccount(newAccountDto, userId);
            return new Result.Success<>("Счёт успешно создан");
        } catch (FeignException.FeignClientException e) {
            return handleFeignException(e);
        } catch (RetryableException | FeignException.InternalServerError e) {
            return new Result.Failure<>(new Exception("Сервис недоступен. Пожалуйста, попробуйте позже."));
        } catch (Exception e) {
            return new Result.Failure<>(new Throwable("Ошибка"));
        }
    }

    @Override
    public Result<Collection<ResponseAccountDto>> getUserAccounts(Long userId) {
        try {
            ResponseEntity<Collection<ResponseAccountDto>> response = middleServiceClient.getUserAccounts(userId);
            return new Result.Success<>(response.getBody());
        } catch (FeignException.FeignClientException e) {
            return handleFeignException(e);
        } catch (RetryableException | FeignException.InternalServerError e) {
            return new Result.Failure<>(new Throwable("Сервис недоступен. Пожалуйста, попробуйте позже."));
        }
    }

    private Result handleFeignException(FeignException.FeignClientException e) {
        if (e.status() == 409) {
            return new Result.Failure<>(new Exception("У Вас уже есть счет"));
        }
        if (e.status() == 404) {
            return new Result.Failure<>(new Exception("Сначала нужно зарегистрироваться."));
        }
        return new Result.Failure<>(new Exception("Ошибка"));
    }
}
