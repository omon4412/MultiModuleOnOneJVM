package ru.omon4412.minibank.middle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTransferRequestDto {
    @NotNull
    @NotBlank
    private String from;
    @NotNull
    @NotBlank
    private String to;
    private BigDecimal amount;
}
