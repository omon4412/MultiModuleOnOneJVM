package ru.omon4412.minibank.middle.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.omon4412.minibank.middle.client.BackendServiceClient;
import ru.omon4412.minibank.middle.dto.UserIdResponseDto;
import ru.omon4412.minibank.middle.dto.UserRequestDto;
import ru.omon4412.minibank.middle.dto.UsernameResponseDto;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(value = "application.services.type", havingValue = "backend")
public class RegistrationServiceImpl implements RegistrationService {
    private final BackendServiceClient backendServiceClient;

    @Override
    public void registerUser(UserRequestDto userRequestDto) {
        backendServiceClient.registerUser(userRequestDto);
    }

    @Override
    public UsernameResponseDto getUsernameById(Long id) {
        return backendServiceClient.getUsernameById(id).getBody();
    }

    @Override
    public UserIdResponseDto getUserIdByUsername(String username) {
        return backendServiceClient.getUserIdByUserName(username).getBody();
    }
}
