package ru.practicum.compilation.service;

import ru.practicum.compilation.model.dto.CompilationInDto;
import ru.practicum.compilation.model.dto.CompilationOutDto;
import ru.practicum.compilation.model.dto.CompilationUpdateDto;

import java.util.List;

public interface CompilationService {

    CompilationOutDto addCompilation(CompilationInDto newCompilation);

    void deleteCompilation(Long compId);

    CompilationOutDto updateCompilation(Long compId, CompilationUpdateDto updateCompilation);

    List<CompilationOutDto> getCompilations(Boolean pinned, int from, int size);

    CompilationOutDto getCompilationById(Long compId);
}

