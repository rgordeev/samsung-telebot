package ru.rgordeev.telebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.telegram.telegrambots.ApiContextInitializer;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableConfigurationProperties
public class TelebotApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelebotApplication.class, args);
    }

    @PostConstruct
    public void init() {
        ApiContextInitializer.init();
    }
}
