package ru.rgordeev.telebot.actions;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {
    Object doAction(Update update);
}
