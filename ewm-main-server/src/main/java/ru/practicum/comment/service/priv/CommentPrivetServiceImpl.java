package ru.practicum.comment.service.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.comment.CommentRepository;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.CommentMapper;
import ru.practicum.comment.model.dto.CommentInDto;
import ru.practicum.comment.model.dto.CommentOutDto;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.common.exception.UnfulfilledConditionException;
import ru.practicum.event.EventRepository;
import ru.practicum.event.model.Event;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static ru.practicum.enums.Status.PUBLISHED;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentPrivetServiceImpl implements CommentPrivetService {
    private final UserRepository userRepo;
    private final EventRepository eventRepo;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepo;

    @Override
    public CommentOutDto addComment(Long userId, Long eventId, CommentInDto comment) {
        User user = userValidator(userId);
        Event event = eventRepo.findById(eventId).orElseThrow(() -> new NotFoundException(
                String.format("Event with id=%s not found", eventId)));
        validatePublishedEvent(event);
        Comment commentSave = commentRepo.save(commentMapper.mapInDtoToEntity(comment, user, event));

        log.info("The comment has been added for an event with an Id {} and a user with an id {}",
                eventId, userId);

        return commentMapper.toDto(commentSave);
    }

    @Override
    public CommentOutDto updateComment(Long userId, Long commentId, CommentInDto updateComment) {
        userValidator(userId);
        Comment comment = commentValidator(commentId);
        authorValidator(userId, comment);
        comment.setText(updateComment.getText());
        comment.setCreated(LocalDateTime.now());

        log.info("The comment with id {} has been updated for an event with id {} ", commentId,
                comment.getEvent().getId());
        return commentMapper.toDto(commentRepo.save(comment));
    }

    @Override
    public CommentOutDto getComment(Long userId, Long comId) {
        return commentMapper.toDto(commentRepo.findByAuthorIdAndId(userId, comId).orElseThrow(() ->
                new NotFoundException(
                        String.format("comment with id %s and to users with id %s not found", comId, userId))));
    }

    @Override
    public List<CommentOutDto> getComments(Long userId) {
        userValidator(userId);
        return commentMapper.mapToListDto(commentRepo.findAllByAuthorId(userId));
    }

    @Override
    public void deleteUserComment(Long userId, Long comId) {
        Comment comment = commentValidator(comId);
        authorValidator(userId, comment);
        commentRepo.deleteById(comment.getId());
        log.info("The comment with id {}, with the author's id {} has been successfully deleted.",
                comment.getId(), userId);
    }

    private void authorValidator(Long userId, Comment comment) {

        if (!Objects.equals(comment.getAuthor().getId(), userId)) {
            throw new UnfulfilledConditionException("the user is not the author of the comment");
        }
    }

    private User userValidator(Long id) {
       return userRepo.findById(id).orElseThrow(() -> new NotFoundException(
                String.format("User with id=%s not found", id)));

    }

    private Comment commentValidator(Long id) {
        return commentRepo.findById(id).orElseThrow(() -> new NotFoundException(
                String.format("Comment with id=%s not found", id)));
    }

    private void validatePublishedEvent(Event event) {
        if (!event.getStatus().equals(PUBLISHED)) {
            throw new UnfulfilledConditionException("Unpublished events cannot be commented on.");
        }
    }
}
