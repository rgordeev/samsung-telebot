package ru.rgordeev.telebot.actions;

public enum Actions {
    START("start"),
    MAKE_A_WISH("makeawish");

    private String title;

    Actions(String title) {
        this.title = title;
    }
}
