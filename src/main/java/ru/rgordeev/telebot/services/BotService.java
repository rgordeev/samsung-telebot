package ru.rgordeev.telebot.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rgordeev.telebot.config.BotProperties;

@Component
@Slf4j
public class BotService extends TelegramLongPollingBot {

    private final BotProperties botProperties;

    public BotService(BotProperties botProperties) {
        this.botProperties = botProperties;
    }

    @Override
    public String getBotToken() {
        return botProperties.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        log.debug("Message {} from {} received!",
                update.getMessage().getText(),
                update.getMessage().getFrom().getFirstName());

        SendMessage message = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText("Hello from bot!");

        try {
            sendApiMethod(message);
        } catch (TelegramApiException e) {
            log.error("Error while sending message to telegram server {}", e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return botProperties.getUserName();
    }
}
