package ru.practicum.comment.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.model.dto.CommentInDto;
import ru.practicum.comment.model.dto.CommentOutDto;
import ru.practicum.comment.service.priv.CommentPrivetService;

import java.util.List;

@RestController
@RequestMapping("/comments/users/{userId}")
@RequiredArgsConstructor
@Validated
public class CommentPrivetController {

    private final CommentPrivetService service;

    @PostMapping("/events/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentOutDto addComment(@PathVariable Long userId,
                                    @PathVariable @Positive Long eventId,
                                    @Valid @RequestBody CommentInDto comment) {
        return service.addComment(userId, eventId, comment);
    }

    @PatchMapping("/{commentId}")
    public CommentOutDto updateComment(@PathVariable @Positive Long userId,
                                       @PathVariable @Positive Long commentId,
                                       @RequestBody @Valid CommentInDto comment) {
        return service.updateComment(userId, commentId, comment);
    }

    @GetMapping("/{commentId}")
    public CommentOutDto getComment(@PathVariable @Positive Long userId,
                                    @PathVariable @Positive Long commentId) {
        return service.getComment(userId, commentId);
    }

    @GetMapping
    public List<CommentOutDto> getComments(@PathVariable @Positive Long userId) {
        return service.getComments(userId);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserComment(@PathVariable @Positive Long userId,
                                  @PathVariable @Positive Long commentId) {
        service.deleteUserComment(userId, commentId);
    }
}
