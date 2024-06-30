package ru.omon4412.minibank.service;

import feign.RetryableException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.omon4412.minibank.middle.client.BackendServiceClient;
import ru.omon4412.minibank.middle.dto.UserRequestDto;
import ru.omon4412.minibank.middle.dto.UsernameResponseDto;
import ru.omon4412.minibank.middle.exception.ConflictException;
import ru.omon4412.minibank.middle.exception.NotFoundException;
import ru.omon4412.minibank.middle.service.RegistrationServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {
    @Mock
    private BackendServiceClient backendServiceClient;

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @Test
    void test_registerUser_success() {
        UserRequestDto userRequestDto = new UserRequestDto();
        ResponseEntity<Void> responseEntity = ResponseEntity.noContent().build();
        when(backendServiceClient.registerUser(any(UserRequestDto.class))).thenReturn(responseEntity);

        registrationService.registerUser(userRequestDto);

        verify(backendServiceClient, times(1)).registerUser(any(UserRequestDto.class));
    }

    @Test
    void test_registerUser_whenUserAlreadyRegistered() {
        ConflictException feignClientException = new ConflictException("");
        when(backendServiceClient.registerUser(any(UserRequestDto.class)))
                .thenThrow(feignClientException);
        assertThrows(ConflictException.class, () -> registrationService.registerUser(new UserRequestDto()));
        verify(backendServiceClient, times(1)).registerUser(any(UserRequestDto.class));
    }


    @Test
    void test_registerUser_whenServerIsDown() {
        UserRequestDto userRequestDto = new UserRequestDto();
        when(backendServiceClient.registerUser(any(UserRequestDto.class))).thenThrow(RetryableException.class);

        assertThrows(RetryableException.class, () -> registrationService.registerUser(userRequestDto));

        verify(backendServiceClient, times(1)).registerUser(any(UserRequestDto.class));
    }

    @Test
    void test_getUsernameById_success() {
        when(backendServiceClient.getUsernameById(1L)).thenReturn(ResponseEntity.ok(new UsernameResponseDto("user1")));
        UsernameResponseDto usernameResponseDto = registrationService.getUsernameById(1L);

        assertEquals(usernameResponseDto.getUserName(), "user1");

        verify(backendServiceClient, times(1)).getUsernameById(1L);
    }

    @Test
    void test_getUsernameById_whenNotFound() {
        NotFoundException feignClientException = new NotFoundException("");
        when(backendServiceClient.getUsernameById(1L)).thenThrow(feignClientException);

        assertThrows(NotFoundException.class, () -> registrationService.getUsernameById(1L));
    }
}