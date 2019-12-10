package ru.rgordeev.telebot.actions;

import org.telegram.telegrambots.meta.api.objects.Update;

public class Start implements Command {

    private String action;

    public Start(String action) {
        this.action = action;
    }

    @Override
    public String doAction(Update update) {
        return "/start - get list of commands\n" +
                "/makeawish - wish a present\n" +
                "/choosemember - choose member\n" +
                "/showwish - show wish of choosen member\n" +
                "/listmembers - list all the members";
    }
}
