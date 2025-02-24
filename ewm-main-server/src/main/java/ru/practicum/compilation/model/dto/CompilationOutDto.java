package ru.practicum.compilation.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.model.dto.EventOutDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompilationOutDto {
    private Long id;
    private String title;
    private Boolean pinned;
    private List<EventOutDto> events;
}
