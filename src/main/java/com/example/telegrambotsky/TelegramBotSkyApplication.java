package com.example.telegrambotsky;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TelegramBotSkyApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelegramBotSkyApplication.class, args);
    }

}
