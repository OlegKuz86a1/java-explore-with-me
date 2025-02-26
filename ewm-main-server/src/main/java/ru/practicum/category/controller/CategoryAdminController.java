package ru.practicum.category.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.model.dto.CategoryInDto;
import ru.practicum.category.model.dto.CategoryOutDto;
import ru.practicum.category.service.CategoryService;

@RestController
@RequestMapping("/admin/categories")
@Validated
@RequiredArgsConstructor
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryOutDto addedCat(@RequestBody @Valid CategoryInDto categoryInDto) {
        return categoryService.addedCat(categoryInDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCat(@PathVariable @Positive Long catId) {
        categoryService.deleteCat(catId);
    }

    @PatchMapping("/{catId}")
    public CategoryOutDto updateCat(@PathVariable @Positive Long catId,
                                    @RequestBody @Valid CategoryInDto categoryInDto) {
        return categoryService.updateCat(catId, categoryInDto);
    }
}
