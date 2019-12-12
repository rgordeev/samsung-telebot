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
import ru.rgordeev.telebot.model.Present;

import java.util.HashMap;
import java.util.Map;

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

        Integer userId = update.getMessage().getFrom().getId();
        BotState botState = DataService.STATES.get(userId);
        SendMessage message =  null;
        if (BotState.WAIT_FOR_PRESENT.equals(botState)) {

            String[] payload = update.getMessage().getText().split(" ");
            if (payload.length > 0 && payload.length < 5) {
                DataService.STATES.put(userId, BotState.FREE);

                Present present = new Present();
                present.setTitle(String.join(" ", payload));

                Person p = DataService.DB.get(userId);
                p.setPresent(present);
            } else {
                message = new SendMessage()
                        .setChatId(update.getMessage().getChatId())
                        .setText(BotState.WAIT_FOR_PRESENT.getPayload());
            }
        } else if (BotState.WAIT_FOR_PERSON.equals(botState)) {
            String text = update.getMessage().getText();
            try {
                Integer id = Integer.valueOf(text);
                Person p = DataService.DB.get(id);
                if (p == null) {
                    String result = DataService.listMembers(userId);
                    message = new SendMessage()
                            .setChatId(update.getMessage().getChatId())
                            .setText(result);
                } else {

                    Person myself = DataService.DB.get(userId);
                    myself.setPresentee(p);
                    p.setPresenter(myself);

                    message = new SendMessage()
                            .setChatId(update.getMessage().getChatId())
                            .setText(String.format("Вы выбрали пользователя %s %s", p.getName(), p.getNickname()));
                    DataService.STATES.put(userId, BotState.FREE);
                }

            } catch (NumberFormatException e) {
                String result = DataService.listMembers(userId);
                message = new SendMessage()
                        .setChatId(update.getMessage().getChatId())
                        .setText(result);
            }
        } else {
            log.debug("Message {} from {} received!",
                    update.getMessage().getText(),
                    update.getMessage().getFrom().getFirstName());

            Command command = buildCommand(update);

            message = new SendMessage()
                    .setChatId(update.getMessage().getChatId())
                    .setText(command.doAction(update).toString());
        }
        try {
            if (message != null)
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

        if (update.getMessage().getText().contains("/choosemember")) {
            return new ChooseMember();
        }
        return null;
    }

    @Override
    public String getBotUsername() {
        return botProperties.getUserName();
    }
}
