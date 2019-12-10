package ru.rgordeev.telebot.actions;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Update;

import static ru.rgordeev.telebot.services.BotService.persons;

@Slf4j
public class ListMembers implements Command {

    //Logger log = LoggerFactory.getLogger(ListMembers.class);

    @Override
    public Object doAction(Update update) {
        String result = persons.entrySet().stream().map(entry -> {
            return String.format("%s %s", entry.getKey(), entry.getValue().toString());
        }).reduce("", (a, b) -> a + b);
        log.debug("result: {}", result);
        return result;
    }
}
