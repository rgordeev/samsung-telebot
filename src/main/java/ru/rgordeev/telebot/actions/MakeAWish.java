package ru.rgordeev.telebot.actions;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.rgordeev.telebot.model.Person;
import ru.rgordeev.telebot.model.Present;

import static ru.rgordeev.telebot.services.BotService.persons;

@Slf4j
public class MakeAWish implements Command {
    private String action;

    public MakeAWish(String action) {
        this.action = action;
    }

    @Override
    public Person doAction(Update update) {

        log.debug("update: {}", update);
        Person p = new Person();
        p.setNickname(update.getMessage().getFrom().getUserName());
        p.setName(update.getMessage().getFrom().getFirstName());

        String[] payload = update.getMessage().getText().split(" ");
        String[] result = new String[payload.length - 1];
        for (int i = 0; i < payload.length - 1; i++) {
            result[i] = payload[i + 1];
        }
        Present present = new Present();
        present.setTitle(String.join(" ", result));

        p.setPresent(present);

        persons.put(p.getNickname(), p);

        log.debug("Person: {}", p);
        return p;
    }
}
