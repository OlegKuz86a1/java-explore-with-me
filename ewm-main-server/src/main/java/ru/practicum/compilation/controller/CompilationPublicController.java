package ru.practicum.compilation.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.model.dto.CompilationOutDto;
import ru.practicum.compilation.service.CompilationService;

import java.util.List;

@RestController
@Validated
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class CompilationPublicController {

    private final CompilationService service;

    @GetMapping
    public List<CompilationOutDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                   @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                   @RequestParam(defaultValue = "10") @Min(1) Integer size) {
       return service.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationOutDto getCompilations(@PathVariable @Positive Long compId) {

        return service.getCompilationById(compId);
    }
}
