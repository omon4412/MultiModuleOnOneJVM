package ru.omon4412.minibank.telegrambot.service;

import ru.omon4412.minibank.telegrambot.dto.UserIdResponseDto;
import ru.omon4412.minibank.telegrambot.dto.UserRequestDto;
import ru.omon4412.minibank.telegrambot.util.Result;

public interface UserRegistrationService {
    Result<String> registerUser(UserRequestDto userRequestDto);

    Result<UserIdResponseDto> getUserIdByUserName(String username);
}
