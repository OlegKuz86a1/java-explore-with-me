package ru.practicum.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {

    REJECTED("REJECTED"),
    PENDING("PENDING"),
    PUBLISHED("PUBLISHED"),
    CANCELED("CANCELED"),
    CONFIRMED("CONFIRMED");

    private final String status;
}
