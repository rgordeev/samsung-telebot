package ru.rgordeev.telebot.services;

import org.springframework.stereotype.Service;
import ru.rgordeev.telebot.model.Person;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DataService {
    public static final Map<Integer, Person> DB = new HashMap<>();
    public static final Map<Integer, BotState> STATES = new HashMap<>();

    public static List<Integer> listMembers(final Integer userId) {
        return DataService.DB.entrySet()
                .stream()
                .filter(e -> !Objects.equals(userId, e.getKey()))
                .filter(e -> {
                    Person p = e.getValue();
                    return p.getPresenter() == null;
                })
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

}
