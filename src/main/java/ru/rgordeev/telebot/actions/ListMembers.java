package ru.rgordeev.telebot.actions;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.rgordeev.telebot.services.DataService;

@Slf4j
public class ListMembers implements Command {

    //Logger log = LoggerFactory.getLogger(ListMembers.class);

    @Override
    public Object doAction(Update update) {
        String result = DataService.DB.entrySet().stream().map(entry -> {
            return String.format("%s %s", entry.getKey(), entry.getValue().toString());
        }).reduce("", (a, b) -> a + b);
        log.debug("result: {}", result);
        return result;
    }
}
