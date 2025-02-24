package ru.practicum.compilation.model;

import org.mapstruct.*;
import ru.practicum.compilation.model.dto.CompilationInDto;
import ru.practicum.compilation.model.dto.CompilationOutDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.dto.EventOutDto;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public abstract class CompilationMapper {


    public CompilationOutDto toDto(Compilation compilation, List<EventOutDto> events) {
        return CompilationOutDto.builder()
                .id(compilation.getId())
                .events(events)
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }

    public Compilation mapToCompilation(CompilationInDto compilation, List<Event> events) {
        return Compilation.builder()
                .events(events)
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }
}
