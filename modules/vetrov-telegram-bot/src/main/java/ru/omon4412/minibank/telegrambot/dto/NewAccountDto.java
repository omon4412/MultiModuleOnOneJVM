package ru.omon4412.minibank.telegrambot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewAccountDto {
    private String accountName;
}
