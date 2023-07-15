package com.example.telegrambotsky.listener;

import com.example.telegrambotsky.service.NotificationTaskService;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TelegramBotListenerTest {
    @Mock
    private TelegramBot telegramBot;
    @Mock
    private NotificationTaskService notificationTaskService;
    @InjectMocks
    private TelegramBotListener telegramBotListener;

    @Test
    public void handleStartTest() throws URISyntaxException, IOException {
        String json;
        json = Files.readString(
                Path.of(TelegramBotListenerTest.class.getResource("update.json").toURI()));
        Update update = BotUtils.fromJson(json.replace("%text%", "/start"), Update.class);

        SendResponse sendResponse = BotUtils.fromJson("""
                {
                    "ok": true
                }          
                """, SendResponse.class);

        when(telegramBot.execute(any())).thenReturn(sendResponse);

        telegramBotListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(update.message().chat().id());
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Привет! Я помогу тебе запланировать задачу. Отправь её в формате 11.07.2023 18:00");

    }
}