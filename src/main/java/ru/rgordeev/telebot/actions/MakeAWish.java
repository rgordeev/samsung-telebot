package ru.rgordeev.telebot.actions;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.rgordeev.telebot.model.Person;
import ru.rgordeev.telebot.model.Present;
import ru.rgordeev.telebot.services.BotState;
import ru.rgordeev.telebot.services.DataService;

@Slf4j
public class MakeAWish implements Command {
    private String action;

    public MakeAWish(String action) {
        this.action = action;
    }

    @Override
    public String doAction(Update update) {

        log.debug("update: {}", update);
        Person p = new Person();
        p.setId(update.getMessage().getFrom().getId());
        p.setNickname(update.getMessage().getFrom().getUserName());
        p.setName(update.getMessage().getFrom().getFirstName());

//        String[] payload = update.getMessage().getText().split(" ");
//        String[] result = new String[payload.length - 1];
//        for (int i = 0; i < payload.length - 1; i++) {
//            result[i] = payload[i + 1];
//        }
//        Present present = new Present();
//        present.setTitle(String.join(" ", result));
//
//        p.setPresent(present);

        DataService.DB.put(p.getId(), p);
        DataService.STATES.put(p.getId(), BotState.WAIT_FOR_PRESENT);

        log.debug("Person: {}", p);
        return BotState.WAIT_FOR_PRESENT.getPayload();
    }
}
