package ru.omon4412.minibank.telegrambot.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.omon4412.minibank.telegrambot.model.TelegramMessage;

@Component
@RequiredArgsConstructor
public class UnknownCommand implements Command {
    @Override
    public TelegramMessage execute(Update update) {
        return new TelegramMessage(update.getMessage().getChatId(), "Неизвестная команда");
    }

    @Override
    public String getCommand() {
        return Commands.UNKNOWN.getCommand();
    }
}
