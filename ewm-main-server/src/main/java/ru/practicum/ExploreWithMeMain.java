package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {"ru.practicum.main", "ru.practicum.common"})
public class ExploreWithMeMain {
    public static void main(String[] args) {
        SpringApplication.run(ExploreWithMeMain.class, args);
    }
}