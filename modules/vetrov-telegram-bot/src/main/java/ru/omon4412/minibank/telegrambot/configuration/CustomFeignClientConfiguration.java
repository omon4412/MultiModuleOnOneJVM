package ru.omon4412.minibank.telegrambot.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomFeignClientConfiguration {

    public CustomFeignClientConfiguration(ObjectMapper objectMapper) {
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new ErrorDecoder.Default();
    }
}