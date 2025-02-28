package ru.practicum.comment.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.model.dto.CommentInDto;
import ru.practicum.comment.model.dto.CommentOutDto;
import ru.practicum.comment.service.admin.CommentAdminService;

import java.util.List;

@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
@Validated
public class CommentAdminController {

    private final CommentAdminService service;

    @GetMapping
    public List<CommentOutDto> commentAdminSearch(@RequestParam String content,
                                                  @RequestParam(defaultValue = "0") @Min(0) int  from,
                                                  @RequestParam(defaultValue = "10") @Min(1)int size) {
        return service.commentAdminSearch(content, from, size);
    }

    @PatchMapping("/{commentId}")
    public CommentOutDto commentAdminUpdate(@PathVariable @Positive Long commentId,
                                        @RequestBody @Valid CommentInDto commentInDto) {
        return service.commentAdminUpdate(commentId, commentInDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAdminComment(@PathVariable @Positive Long commentId) {
        service.deleteAdminComment(commentId);
    }
}
