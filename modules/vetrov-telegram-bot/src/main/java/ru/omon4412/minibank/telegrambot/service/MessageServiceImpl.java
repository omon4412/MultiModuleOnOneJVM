package ru.omon4412.minibank.telegrambot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.omon4412.minibank.telegrambot.bot.MiniBankBot;
import ru.omon4412.minibank.telegrambot.model.TelegramMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {
    private MiniBankBot miniBankBot;

    @Override
    public void sendMessage(TelegramMessage telegramMessage) {
        Long chatId = telegramMessage.chatId();
        String message = telegramMessage.message();

        if (message.isBlank()) {
            log.warn("Получено пустое сообщение");
            return;
        }

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(message);

        try {
            miniBankBot.execute(sendMessage);
            log.info("Сообщение '{}' отправлено в чат '{}'", message, chatId);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Autowired
    public void setMiniBankBot(@Lazy MiniBankBot miniBankBot) {
        this.miniBankBot = miniBankBot;
    }
}
