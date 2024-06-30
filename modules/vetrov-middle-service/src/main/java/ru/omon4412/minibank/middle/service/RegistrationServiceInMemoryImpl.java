package ru.omon4412.minibank.middle.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.omon4412.minibank.middle.dto.UserIdResponseDto;
import ru.omon4412.minibank.middle.dto.UserRequestDto;
import ru.omon4412.minibank.middle.dto.UsernameResponseDto;
import ru.omon4412.minibank.middle.exception.ConflictException;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@ConditionalOnProperty(value = "application.services.type", havingValue = "inMemory")
public class RegistrationServiceInMemoryImpl implements RegistrationService {
    private final Map<Long, UserRequestDto> users = new ConcurrentHashMap<>();

    @Override
    public void registerUser(UserRequestDto userRequestDto) {
        if (users.containsKey(userRequestDto.getUserId())) {
            throw new ConflictException("Пользователь уже зарегистрирован");
        }
        users.put(userRequestDto.getUserId(), userRequestDto);
    }

    @Override
    public UsernameResponseDto getUsernameById(Long id) {
        UserRequestDto userRequestDto = users.get(id);
        if (userRequestDto == null) {
            return null;
        }
        return new UsernameResponseDto(userRequestDto.getUserName());
    }

    @Override
    public UserIdResponseDto getUserIdByUsername(String username) {
        Optional<UserRequestDto> userRequestDto = users.values().stream()
                .filter(u -> u.getUserName()
                        .equals(username))
                .findFirst();
        return userRequestDto.map(requestDto -> new UserIdResponseDto(requestDto.getUserId())).orElse(null);
    }
}
