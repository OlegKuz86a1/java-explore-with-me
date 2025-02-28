package ru.practicum.comment.service.priv;

import ru.practicum.comment.model.dto.CommentInDto;
import ru.practicum.comment.model.dto.CommentOutDto;

import java.util.List;

public interface CommentPrivateService {

    CommentOutDto addComment(Long userId, Long eventId, CommentInDto comment);

    CommentOutDto updateComment(Long userId, Long commentId, CommentInDto updateComment);

    List<CommentOutDto> getComments(Long userId);

    CommentOutDto getComment(Long userId, Long comId);

    void deleteUserComment(Long userId, Long comId);
}
