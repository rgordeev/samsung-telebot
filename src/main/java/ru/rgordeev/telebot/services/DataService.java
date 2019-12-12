package ru.rgordeev.telebot.services;

import org.springframework.stereotype.Service;
import ru.rgordeev.telebot.model.Person;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class DataService {
    public static final Map<Integer, Person> DB = new HashMap<>();
    public static final Map<Integer, BotState> STATES = new HashMap<>();

    public static String listMembers(final Integer userId) {
        String numbers = DataService.DB.entrySet()
                .stream()
                .filter(e -> !Objects.equals(userId, e.getKey()))
                .filter(e -> {
                    Person p = e.getValue();
                    return p.getPresenter() == null;
                })
                .map(e -> e.getKey().toString())
                .reduce("", (a, b) -> String.format("%s, %s", a, b));

        return "Выберете адресанта: " +  numbers;
    }

}
