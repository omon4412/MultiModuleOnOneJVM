package ru.omon4412.minibank.telegrambot.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.omon4412.minibank.telegrambot.model.TelegramMessage;

public interface Command {
    TelegramMessage execute(Update update);

    String getCommand();
}
