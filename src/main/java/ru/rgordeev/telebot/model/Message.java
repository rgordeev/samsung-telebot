package ru.rgordeev.telebot.model;

import org.checkerframework.checker.units.qual.C;

import javax.persistence.*;

@Entity(name = "messages")
public class Message {

    @Id
    @SequenceGenerator(name="message_id_gen", sequenceName = "message_id_seq")
    @GeneratedValue(generator = "message_id_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_name", length = 255)
    private String userName;

    @Column(name = "message_text", columnDefinition = "text")
    private String text;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
