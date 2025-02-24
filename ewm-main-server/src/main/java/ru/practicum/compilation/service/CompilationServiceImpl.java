package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.compilation.CompilationRepository;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.model.CompilationMapper;
import ru.practicum.compilation.model.dto.CompilationInDto;
import ru.practicum.compilation.model.dto.CompilationOutDto;
import ru.practicum.compilation.model.dto.CompilationUpdateDto;
import ru.practicum.event.EventRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventMapper;
import ru.practicum.event.model.dto.EventOutDto;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {
    private final EventRepository eventRepo;
    private final CompilationMapper compilationMapper;
    private final CompilationRepository compilationRepo;
    private final EventMapper eventMapper;

    @Override
    public CompilationOutDto addCompilation(CompilationInDto newCompilation) {
        List<Event> events = newCompilation.getEvents() == null ? new ArrayList<>() :
                eventRepo.findAllById(newCompilation.getEvents());

        Compilation compilation = compilationRepo.save(compilationMapper.mapToCompilation(newCompilation, events));
        log.info("compilation of events added with ID {} with the number of events {}", compilation.getId(),
                compilation.getEvents().size());

        List<EventOutDto> eventOutDtoList = eventMapper.toListDto(compilation.getEvents());
        return compilationMapper.toDto(compilation, eventOutDtoList);
    }

    @Override
    public void deleteCompilation(Long compId) {
        compilationValidator(compId);
        compilationRepo.deleteById(compId);
        log.info("compilation with id {} has been deleted", compId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public CompilationOutDto updateCompilation(Long compId, CompilationUpdateDto updateCompilation) {
        Compilation compilation = compilationValidator(compId);

        if (updateCompilation.getEvents() != null) {
            compilation.setEvents(eventRepo.findAllById(updateCompilation.getEvents()));
        }
        if (updateCompilation.getTitle() != null && !updateCompilation.getTitle().equals(compilation.getTitle())) {
            compilation.setTitle(updateCompilation.getTitle());
        }
        if (updateCompilation.getPinned() != null && updateCompilation.getPinned() != compilation.getPinned()) {
            compilation.setPinned(updateCompilation.getPinned());
        }

        Compilation saved = compilationRepo.save(compilation);
        log.info("The Compilation with id {} has been updated", saved.getId());

        return compilationMapper.toDto(saved, eventMapper.toListDto(saved.getEvents()));
    }

    @Override
    public List<CompilationOutDto> getCompilations(Boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        List<Compilation> compilations = pinned == null ? compilationRepo.findAll(pageable).getContent() :
                compilationRepo.findAllByPinned(pinned, pageable);

        return compilations.stream()
                .map(compilation -> {
                    List<EventOutDto> events = compilation.getEvents().stream()
                            .map(eventMapper::toDto)
                            .collect(Collectors.toList());
                    return compilationMapper.toDto(compilation, events);
                })
                .collect(Collectors.toList());

    }

    @Override
    public CompilationOutDto getCompilationById(Long compId) {
        Compilation compilation = compilationValidator(compId);
        return compilationMapper.toDto(compilation, eventMapper.toListDto(compilation.getEvents()));
    }

    private Compilation compilationValidator(Long compId) {
      return compilationRepo.findById(compId).orElseThrow(() -> new NotFoundException(
                String.format("User with id=%s not found", compId)));
    }
}
