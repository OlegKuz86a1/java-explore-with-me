package ru.practicum.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StateAction {
    PUBLISH_EVENT("PUBLISH_EVENT"),
    REJECT_EVENT("REJECT_EVENT"),
    SEND_TO_REVIEW("SEND_TO_REVIEW"),
    CANCEL_REVIEW("CANCEL_REVIEW");

    private final String state;
}
