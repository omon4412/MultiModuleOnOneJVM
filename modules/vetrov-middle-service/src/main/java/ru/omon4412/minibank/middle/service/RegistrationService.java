package ru.omon4412.minibank.middle.service;

import ru.omon4412.minibank.middle.dto.UserIdResponseDto;
import ru.omon4412.minibank.middle.dto.UserRequestDto;
import ru.omon4412.minibank.middle.dto.UsernameResponseDto;

public interface RegistrationService {

    void registerUser(UserRequestDto userRequestDto);

    UsernameResponseDto getUsernameById(Long id);

    UserIdResponseDto getUserIdByUsername(String username);
}
