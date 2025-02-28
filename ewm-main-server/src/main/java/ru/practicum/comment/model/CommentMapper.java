package ru.practicum.comment.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.practicum.comment.model.dto.CommentInDto;
import ru.practicum.comment.model.dto.CommentOutDto;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public abstract class CommentMapper  {

    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "author", source = "author.id")
    public abstract CommentOutDto toDto(Comment entity);

    public static Comment mapInDtoToEntity(CommentInDto commentInDto, User author, Event event) {
        return Comment.builder()
                .event(event)
                .author(author)
                .content(commentInDto.getContent())
                .created(LocalDateTime.now())
                .build();
    }

    public List<CommentOutDto> mapToListDto(List<Comment> commentList) {
        return commentList.stream()
                .map(this::toDto)
                .toList();

    }
}