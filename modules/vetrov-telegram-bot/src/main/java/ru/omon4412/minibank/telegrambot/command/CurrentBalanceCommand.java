package ru.omon4412.minibank.telegrambot.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.omon4412.minibank.telegrambot.dto.ResponseAccountDto;
import ru.omon4412.minibank.telegrambot.model.TelegramMessage;
import ru.omon4412.minibank.telegrambot.service.MiddleServiceGateway;
import ru.omon4412.minibank.telegrambot.util.Result;

import java.math.BigDecimal;
import java.util.Collection;

@Component
@RequiredArgsConstructor
class CurrentBalanceCommand implements Command {
    private final MiddleServiceGateway middleServiceGateway;

    @Override
    public TelegramMessage execute(Update update) {
        Long userId = update.getMessage().getFrom().getId();
        String username = update.getMessage().getFrom().getUserName();
        if (username == null) {
            return new TelegramMessage(update.getMessage().getChatId(),
                    "Для работы с ботом вам нужен telegram username");
        }

        Result<Collection<ResponseAccountDto>> responseResult = middleServiceGateway.getUserAccounts(userId);
        StringBuilder message = new StringBuilder();
        if (responseResult.isFailure()) {
            message.append(responseResult.exceptionOrNull().getMessage());
        } else {
            Collection<ResponseAccountDto> accounts = responseResult.getOrNull();
            if (accounts.isEmpty()) {
                message.append("Нет активных счетов.");
            } else {
                message.append("Ваши активные счета:\n");
                message.append("--------------------------------------------\n");
                for (ResponseAccountDto account : accounts) {
                    message.append(" - ");
                    message.append(account.getAccountName());
                    message.append(" - ");
                    message.append(account.getAmount());
                    message.append(" рублей\n");
                }
                message.append("--------------------------------------------\n");
                message.append("Сумма по счетам: ");
                message.append(accounts.stream()
                        .map(ResponseAccountDto::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));
                message.append(" рублей\n");
            }
        }
        return new TelegramMessage(update.getMessage().getChatId(), message.toString());
    }

    @Override
    public String getCommand() {
        return Commands.CURRENTBALANCE.getCommand();
    }
}
