package ru.practicum.comment.service.admin;

import ru.practicum.comment.model.dto.CommentInDto;
import ru.practicum.comment.model.dto.CommentOutDto;

import java.util.List;

public interface CommentAdminService {

    List<CommentOutDto> commentAdminSearch(String text, int from, int size);

    CommentOutDto commentAdminUpdate(Long commentId, CommentInDto commentInDto);

    void deleteAdminComment(Long comId);
}
