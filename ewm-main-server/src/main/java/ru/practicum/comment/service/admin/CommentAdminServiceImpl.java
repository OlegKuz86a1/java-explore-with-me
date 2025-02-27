package ru.practicum.comment.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.comment.CommentRepository;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.CommentMapper;
import ru.practicum.comment.model.dto.CommentInDto;
import ru.practicum.comment.model.dto.CommentOutDto;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.event.EventRepository;
import ru.practicum.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentAdminServiceImpl implements CommentAdminService {

    private final UserRepository userRepo;
    private final EventRepository eventRepo;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepo;

    @Override
    public List<CommentOutDto> commentAdminSearch(String text, int from, int size) {

        if (text.isBlank()) {
            ResponseEntity
                    .status(OK)
                    .body(new ArrayList<>());
        }

        Pageable pageable = PageRequest.of(from, size);
        List<Comment> searchComments = commentRepo.findCommentsByText(text, pageable).getContent();

        log.info("The comment with text {}, has been successfully received", text);
        return commentMapper.mapToListDto(searchComments);
    }

    @Override
    public CommentOutDto commentAdminUpdate(Long commentId, CommentInDto commentInDto) {
        Comment comment = commentValidator(commentId);
        comment.setText(commentInDto.getText());
        CommentOutDto commentOutDto = commentMapper.toDto(commentRepo.save(comment));

        log.info("The comment with id {} has been updated", commentId);
        return commentOutDto;
    }

    @Override
    public void deleteAdminComment(Long commentId) {
        Comment comment = commentValidator(commentId);
        commentRepo.deleteById(comment.getId());
        log.info("The comment with id {}, has been successfully deleted.", comment.getId());
    }

    private Comment commentValidator(Long id) {
        return commentRepo.findById(id).orElseThrow(() -> new NotFoundException(
                String.format("Comment with id=%s not found", id)));
    }
}
