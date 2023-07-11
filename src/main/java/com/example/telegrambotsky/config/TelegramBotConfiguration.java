package com.example.telegrambotsky.config;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//класс для соединения с тб
@Configuration
public class TelegramBotConfiguration  {
    //создание объекта для взаимедойствия с ботом
    //передача токена через настойки конфигурации
    @Bean
    public TelegramBot telegramBot(@Value("${telegram.bot.token}")String token) {
        return new TelegramBot(token);
    }
}
