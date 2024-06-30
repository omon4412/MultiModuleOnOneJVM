package ru.omon4412.minibank.middle.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAccountDto {
    private String accountId;
    private String accountName;
    private BigDecimal amount;
}
