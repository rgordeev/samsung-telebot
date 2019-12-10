package ru.rgordeev.telebot.model;

public class Person {
    private String nickname;
    private String name;
    private Present present;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Present getPresent() {
        return present;
    }

    public void setPresent(Present present) {
        this.present = present;
    }

    @Override
    public String toString() {
        return present.toString();
    }
}
