package ru.rgordeev.telebot.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.rgordeev.telebot.actions.*;
import ru.rgordeev.telebot.config.BotProperties;
import ru.rgordeev.telebot.model.Message;
import ru.rgordeev.telebot.model.Person;
import ru.rgordeev.telebot.model.Present;
import ru.rgordeev.telebot.repositories.MessageRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.rgordeev.telebot.services.BotState.FREE;

@Component
@Slf4j
public class BotService extends TelegramLongPollingBot {

    private final BotProperties botProperties;
    private final MessageRepository messageRepository;

    public BotService(BotProperties botProperties, MessageRepository messageRepository) {
        this.botProperties = botProperties;
        this.messageRepository = messageRepository;
    }

    @Override
    public String getBotToken() {
        return botProperties.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update == null || update.getMessage() == null)
            return;

        Integer userId = update.getMessage().getFrom().getId();

        Message m = new Message();
        m.setUserId(update.getMessage().getFrom().getId());
        m.setUserName(update.getMessage().getFrom().getUserName());
        m.setText(update.getMessage().getText());

        messageRepository.save(m);

        BotState botState = DataService.STATES.get(userId);
        if (botState == null || botState.equals(FREE)) {
            KeyboardRow row = new KeyboardRow();
            row.add("Начать");

            List<KeyboardRow> keyboardRows = new ArrayList<>();
            keyboardRows.add(row);
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup().setKeyboard(keyboardRows);

            SendMessage keyboard = new SendMessage()
                    .setText("Начать работу с ботом?")
                    .setChatId(update.getMessage().getChatId())
                    .setReplyMarkup(keyboardMarkup);
            DataService.STATES.put(userId, BotState.WAIT_FOR_PRESENT);
            try {
                sendApiMethod(keyboard);
            } catch (TelegramApiException e) {
                log.error("Error while sending message to telegram server {}", e.getMessage());
            }
        }

        if (BotState.WAIT_FOR_PRESENT.equals(botState)) {
            SendMessage msg = new MakeAWish("").doAction(update);
            try {
                sendApiMethod(msg);
                DataService.STATES.put(userId, BotState.INPUT_FOR_PRESENT);
            } catch (TelegramApiException e) {
                log.error("Error while sending message to telegram server {}", e.getMessage());
            }
        }
        else if (BotState.INPUT_FOR_PRESENT.equals(botState)) {
            String title = update.getMessage().getText();
            Present present = new Present();
            present.setTitle(title);
            Person p = DataService.DB.get(userId);
            p.setPresent(present);

            DataService.STATES.put(userId, BotState.WAIT_FOR_PERSON);
        }
        else if (BotState.WAIT_FOR_PERSON.equals(botState)) {
            String text = update.getMessage().getText();
            try {
                Integer id = Integer.valueOf(text);
                Person p = DataService.DB.get(id);
                if (p == null) {
                    offerPersonsToChoose(update);
                } else {

                    choosePerson(update, userId, p);
                }

            } catch (NumberFormatException e) {
                offerPersonsToChoose(update);
            }
        }
//        else {
//            log.debug("Message {} from {} received!",
//                    update.getMessage().getText(),
//                    update.getMessage().getFrom().getFirstName());
//
//            Command command = buildCommand(update);
//
//            message = new SendMessage()
//                    .setChatId(update.getMessage().getChatId())
//                    .setText(command.doAction(update).toString());
//        }
//        try {
//            if (message != null)
//                sendApiMethod(message);
//        } catch (TelegramApiException e) {
//            log.error("Error while sending message to telegram server {}", e.getMessage());
//        }
    }

    private void choosePerson(Update update, Integer userId, Person p) {
        Person myself = DataService.DB.get(userId);
        myself.setPresentee(p);
        p.setPresenter(myself);

        SendMessage message = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText(String.format("Вы выбрали пользователя %s %s", p.getName(), p.getNickname()));
        DataService.STATES.put(userId, FREE);

        try {
            sendApiMethod(message);
        } catch (TelegramApiException e) {
            log.error("Error while sending message to telegram server {}", e.getMessage());
        }
    }

//    private Command buildCommand(Update update) {
//        if (update.getMessage().getText().contains("/start")) {
//            return new Start(Actions.START.toString());
//        }
//        if (update.getMessage().getText().contains("/makeawish")) {
//            return new MakeAWish(Actions.MAKE_A_WISH.toString());
//        }
//        if (update.getMessage().getText().contains("/listmembers")) {
//            return new ListMembers();
//        }
//
//        if (update.getMessage().getText().contains("/choosemember")) {
//            return new ChooseMember();
//        }
//        return null;
//    }

    @Override
    public String getBotUsername() {
        return botProperties.getUserName();
    }

    private void offerPersonsToChoose(Update update) {
        SendMessage message = new ChooseMember().doAction(update);

        try {
            sendApiMethod(message);
        } catch (TelegramApiException e) {
            log.error("Error while sending message to telegram server {}", e.getMessage());
        }
    }
}
