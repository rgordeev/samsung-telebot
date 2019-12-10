package ru.rgordeev.telebot.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rgordeev.telebot.actions.*;
import ru.rgordeev.telebot.config.BotProperties;
import ru.rgordeev.telebot.model.Person;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class BotService extends TelegramLongPollingBot {

    private final BotProperties botProperties;

    public static Map<String, Person> persons = new HashMap<>();

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

        Command command = buildCommand(update);

        SendMessage message = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText(command.doAction(update).toString());

        try {
            sendApiMethod(message);
        } catch (TelegramApiException e) {
            log.error("Error while sending message to telegram server {}", e.getMessage());
        }
    }

    private Command buildCommand(Update update) {
        if (update.getMessage().getText().contains("/start")) {
            return new Start(Actions.START.toString());
        }
        if (update.getMessage().getText().contains("/makeawish")) {
            return new MakeAWish(Actions.MAKE_A_WISH.toString());
        }
        if (update.getMessage().getText().contains("/listmembers")) {
            return new ListMembers();
        }

        return null;
    }

    @Override
    public String getBotUsername() {
        return botProperties.getUserName();
    }
}
