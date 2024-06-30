package ru.omon4412.minibank.telegrambot.service;

import ru.omon4412.minibank.telegrambot.model.TelegramMessage;

public interface MessageService {

    void sendMessage(TelegramMessage telegramMessage);
}
