package ru.rgordeev.telebot.model;

public class Person {
    private Integer id;
    private String nickname;
    private String name;
    private Present present;
    private Person presentee;
    private Person presenter;

    public Person getPresenter() {
        return presenter;
    }

    public void setPresenter(Person presenter) {
        this.presenter = presenter;
    }

    public Person getPresentee() {
        return presentee;
    }

    public void setPresentee(Person presentee) {
        this.presentee = presentee;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
}
