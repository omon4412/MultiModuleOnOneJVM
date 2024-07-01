package ru.omon4412.minibank.onemodule;

import org.springframework.boot.builder.SpringApplicationBuilder;
import ru.omon4412.minibank.middle.MiddleServiceApplication;
import ru.omon4412.minibank.telegrambot.TelegramBotApplication;


public class MultiModulesInOneJVMApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(MiddleServiceApplication.class)
                .profiles("middle")
                .run(args);
        new SpringApplicationBuilder()
                .sources(TelegramBotApplication.class)
                .profiles("telegrambot")
                .run(args);
    }
}
