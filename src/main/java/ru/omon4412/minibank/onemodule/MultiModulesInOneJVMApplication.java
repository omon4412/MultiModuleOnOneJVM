package ru.omon4412.minibank.onemodule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.omon4412.minibank.middle.MiddleServiceApplication;
import ru.omon4412.minibank.telegrambot.TelegramBotApplication;

@SpringBootApplication
public class MultiModulesInOneJVMApplication {

    public static void main(String[] args) {
        SpringApplication.run(new Class[]{MultiModulesInOneJVMApplication.class, MiddleServiceApplication.class, TelegramBotApplication.class}, args);
    }
}
