package ru.rgordeev.telebot.services;

public enum BotState {
    FREE("Привет! Это бот \"Секретный Санта\"!"),
    WAIT_FOR_PRESENT("Назовите желаемый подарок"),
    INPUT_FOR_PRESENT("Выбор"),
    WAIT_FOR_PERSON("Выберете человека, которому будете дарить подарок");

    private String payload;

    BotState(String payload) {
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }


}
