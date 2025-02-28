package ru.practicum.comment.service.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.CommentRepository;
import ru.practicum.comment.model.CommentMapper;
import ru.practicum.comment.model.dto.CommentOutDto;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.event.EventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class CommentPublicServiceImpl implements CommentPublicService {
    private final EventRepository eventRepo;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepo;


    @Override
    public List<CommentOutDto> getEventComments(Long eventId, int from, int size) {

        if (!eventRepo.existsById(eventId)) {
            throw new NotFoundException(String.format("Event with id=%s not found", eventId));
        }

        Pageable pageable = PageRequest.of(from, size);
        return commentMapper.mapToListDto(commentRepo.findAllByEventId(eventId, pageable).getContent());
    }
}
