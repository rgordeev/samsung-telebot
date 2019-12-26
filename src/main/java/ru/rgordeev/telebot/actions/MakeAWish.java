package ru.rgordeev.telebot.actions;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.rgordeev.telebot.model.Person;
import ru.rgordeev.telebot.model.Present;
import ru.rgordeev.telebot.services.BotState;
import ru.rgordeev.telebot.services.DataService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MakeAWish implements Command {
    private String action;

    public MakeAWish(String action) {
        this.action = action;
    }

    @Override
    public SendMessage doAction(Update update) {

        log.debug("update: {}", update);
        Person p = new Person();
        p.setId(update.getMessage().getFrom().getId());
        p.setNickname(update.getMessage().getFrom().getUserName());
        p.setName(update.getMessage().getFrom().getFirstName());

        KeyboardRow row = new KeyboardRow();
        row.add("iPhone");
        row.add("MacBook");
        row.add("iMac");
        row.add("BMW");

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        keyboardRows.add(row);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup().setKeyboard(keyboardRows);

        SendMessage keyboard = new SendMessage()
                .setText("Выберете подарок")
                .setChatId(update.getMessage().getChatId())
                .setReplyMarkup(keyboardMarkup);



        DataService.DB.put(p.getId(), p);
        DataService.STATES.put(p.getId(), BotState.WAIT_FOR_PRESENT);

        return keyboard;
    }
}
