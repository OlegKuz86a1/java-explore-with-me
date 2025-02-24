package ru.practicum.category.model;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.practicum.category.model.dto.CategoryInDto;
import ru.practicum.category.model.dto.CategoryOutDto;
import ru.practicum.common.EntityMapper;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public abstract class CategoryMapper implements EntityMapper<CategoryOutDto, Category> {

    public abstract Category mapToCategory(CategoryInDto category);

}
