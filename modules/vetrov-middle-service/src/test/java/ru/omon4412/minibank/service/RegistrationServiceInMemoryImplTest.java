package ru.omon4412.minibank.service;

import org.junit.jupiter.api.Test;
import ru.omon4412.minibank.middle.dto.UserRequestDto;
import ru.omon4412.minibank.middle.exception.ConflictException;
import ru.omon4412.minibank.middle.service.RegistrationServiceInMemoryImpl;

import static org.junit.jupiter.api.Assertions.assertThrows;

class RegistrationServiceInMemoryImplTest {

    private final RegistrationServiceInMemoryImpl registrationService = new RegistrationServiceInMemoryImpl();

    @Test
    void test_registerUser_success() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setUserId(101010L);
        registrationService.registerUser(userRequestDto);
    }

    @Test
    void test_registerUser_whenUserAlreadyRegistered() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setUserId(101010L);
        registrationService.registerUser(userRequestDto);
        assertThrows(ConflictException.class, () -> registrationService.registerUser(userRequestDto));
    }
}