package ru.practicum.comment.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.model.dto.CommentOutDto;
import ru.practicum.comment.service.pub.CommentPublicService;

import java.util.List;

@RestController
@RequestMapping("/comments/events")
@RequiredArgsConstructor
@Validated
public class CommentPublicController {

    private final CommentPublicService service;

    @GetMapping("/{eventId}")
    public List<CommentOutDto> getEventComments(@PathVariable @Positive Long eventId,
                                                @RequestParam(defaultValue = "0") @Min(0) int  from,
                                                @RequestParam(defaultValue = "10") @Min(1) int size) {
        return service.getEventComments(eventId, from, size);
    }
}
