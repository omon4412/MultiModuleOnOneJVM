package ru.omon4412.minibank.middle.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Configuration;
import ru.omon4412.minibank.middle.exception.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class CustomErrorDecoder implements ErrorDecoder {
    private final ObjectMapper objectMapper;

    public CustomErrorDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        String errorMessage = "";
        try {
            if (response.body() != null) {
                String responseBody = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
                ApiError apiError = objectMapper.readValue(responseBody, ApiError.class);
                errorMessage = apiError.getError();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return switch (response.status()) {
            case 409 -> new ConflictException(errorMessage);
            case 404 -> new NotFoundException(errorMessage);
            case 503 -> new ServerNotAvailableException(errorMessage);
            default -> new InternalServerErrorException(errorMessage);
        };
    }
}
