package ru.rgordeev.telebot.actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {
    SendMessage doAction(Update update);
}
