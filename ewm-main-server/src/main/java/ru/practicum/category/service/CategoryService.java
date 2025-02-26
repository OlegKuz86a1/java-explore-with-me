package ru.practicum.category.service;

import ru.practicum.category.model.dto.CategoryInDto;
import ru.practicum.category.model.dto.CategoryOutDto;

import java.util.List;

public interface CategoryService {

    CategoryOutDto addedCat(CategoryInDto categoryInDto);

    void deleteCat(Long catId);

    CategoryOutDto updateCat(Long catId, CategoryInDto categoryInDto);

    List<CategoryOutDto> getCategories(int from, int size);

    CategoryOutDto getCategory(Long catId);

}
