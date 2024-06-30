package ru.omon4412.minibank.contract;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.omon4412.minibank.BaseContextTest;
import ru.omon4412.minibank.middle.dto.UserIdResponseDto;
import ru.omon4412.minibank.middle.dto.UserRequestDto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class RegistrationControllerIntegrationTest extends BaseContextTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static Long userId = 1L;

    @Test
    public void registerUser_shouldReturnNoContent() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setUserId(userId);
        userRequestDto.setUserName("TestUser" + userId++);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getUserIdByUserName_shouldReturnUserId() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setUserId(userId);
        userRequestDto.setUserName("TestUser" + userId);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isNoContent());
        String contentAsString = mockMvc.perform(get("/users/" + "TestUser" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserRequestDto())))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserIdResponseDto userIdResponseDto = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });

        assertThat(userIdResponseDto.getUserId()).isEqualTo(userId++);
    }
}
