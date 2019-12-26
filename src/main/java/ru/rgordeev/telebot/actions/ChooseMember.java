package ru.rgordeev.telebot.actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.rgordeev.telebot.model.Person;
import ru.rgordeev.telebot.services.BotState;
import ru.rgordeev.telebot.services.DataService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChooseMember implements Command {
    @Override
    public SendMessage doAction(Update update) {

        Integer userId = update.getMessage().getFrom().getId();
        List<Integer> personsToChoose = DataService.listMembers(userId);;

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = null;
        for (int i = 0; i < personsToChoose.size(); i++) {
            if (i % 4 == 0) {
                row = new KeyboardRow();
                row.add(personsToChoose.get(i).toString());
                keyboardRows.add(row);
            } else {
                row.add(personsToChoose.get(i).toString());
            }
        }
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup().setKeyboard(keyboardRows);

        SendMessage keyboard = new SendMessage()
                .setText("Выберете человека")
                .setChatId(update.getMessage().getChatId())
                .setReplyMarkup(keyboardMarkup);
        return keyboard;
    }
}
