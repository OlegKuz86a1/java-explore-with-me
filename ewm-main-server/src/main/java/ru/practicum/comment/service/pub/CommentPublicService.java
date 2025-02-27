package ru.practicum.comment.service.pub;

import ru.practicum.comment.model.dto.CommentOutDto;

import java.util.List;

public interface CommentPublicService {

    List<CommentOutDto> getEventComments(Long eventId, int from, int size);

}
