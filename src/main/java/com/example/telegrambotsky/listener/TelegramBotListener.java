package com.example.telegrambotsky.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Класс для обработки апдейтов
@Component
public class TelegramBotListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotListener.class);
    private final TelegramBot telegramBot;
    private final Pattern pattern = Pattern.compile("\\d{1,2}\\.\\d{1,2}\\.\\d{4} \\d{1,2}:\\d{2}\\s+ ([А-я\\d\\s.,!?:]+)");
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyy HH:mm");

    public TelegramBotListener(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        try {
            updates.forEach(update -> {
                logger.info("Handles update:{}", updates);

                Message message = update.message();
                Long chatId = message.chat().id();
                String text = message.text();

                if ("/start".equals(text)) {
                    sendMessage(chatId, "Привет! Я помогу тебе запланировать задачу. Отправь её в формате 11.07.2023 18:00");
                } else if (text != null) {
                    Matcher matcher = pattern.matcher(text);
                    if (matcher.find()) {
                        LocalDateTime dateTime = parse(matcher.group(1));
                        if (Objects.isNull(dateTime)) {
                            sendMessage(chatId, "Некорректный формат даты/времени");
                        } else {
                            String txt = matcher.group(2);

                        }
                    } else {
                        sendMessage(chatId, "Некорректный формат сообщения");

                    }
                }
            });
        } catch (Exception e) {
            logger.info(e.getMessage(), e);
        }

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Nullable
    private LocalDateTime parse(String dateTime) {
        try {
            return LocalDateTime.parse(dateTime, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private void sendMessage(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message :{}", sendResponse.message());
        }
    }

}
