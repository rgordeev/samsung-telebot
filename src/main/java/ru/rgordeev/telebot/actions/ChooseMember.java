package ru.rgordeev.telebot.actions;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.rgordeev.telebot.model.Person;
import ru.rgordeev.telebot.services.BotState;
import ru.rgordeev.telebot.services.DataService;

import java.util.Objects;

public class ChooseMember implements Command {
    @Override
    public Object doAction(Update update) {

        Integer userId = update.getMessage().getFrom().getId();
        Person person = DataService.DB.get(userId);
        if (person == null)
            return "Сперва укажите желаемый подарок";

        DataService.STATES.put(userId, BotState.WAIT_FOR_PERSON);

        return DataService.listMembers(userId);
    }
}
