package ru.practicum.compilation.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.model.dto.CompilationInDto;
import ru.practicum.compilation.model.dto.CompilationOutDto;
import ru.practicum.compilation.model.dto.CompilationUpdateDto;
import ru.practicum.compilation.service.CompilationService;

@RestController
@Validated
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class CompilationAdminController {

    private final CompilationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationOutDto addCompilation(@RequestBody @Valid CompilationInDto newCompilation) {
        return service.addCompilation(newCompilation);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable @Positive Long compId) {
        service.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationOutDto updateCompilation(@PathVariable @Positive Long compId,
                                               @Valid @RequestBody CompilationUpdateDto updateCompilation) {
        return service.updateCompilation(compId, updateCompilation);
    }
}
