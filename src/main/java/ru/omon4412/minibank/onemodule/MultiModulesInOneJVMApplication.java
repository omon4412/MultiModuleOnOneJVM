package ru.omon4412.minibank.onemodule;

import org.springframework.boot.SpringApplication;
import ru.omon4412.minibank.middle.MiddleServiceApplication;
import ru.omon4412.minibank.telegrambot.TelegramBotApplication;


public class MultiModulesInOneJVMApplication {
    public static void main(String[] args) {
        SpringApplication.run(new Class[]{MiddleServiceApplication.class, TelegramBotApplication.class}, args);
    }
}
