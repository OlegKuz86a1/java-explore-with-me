package ru.practicum.category.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.category.CategoryRepository;
import ru.practicum.category.model.Category;
import ru.practicum.category.model.CategoryMapper;
import ru.practicum.category.model.dto.CategoryInDto;
import ru.practicum.category.model.dto.CategoryOutDto;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.common.exception.UnfulfilledConditionException;
import ru.practicum.event.EventRepository;

import java.util.List;

@Service
@Slf4j
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepository categoryRepo;
    private final CategoryMapper categoryMapper;
    private final EventRepository eventRepo;

    @Autowired
    public CategoryServiceImp(CategoryRepository categoryRepo, CategoryMapper categoryMapper,
                              EventRepository eventRepo) {
        this.categoryRepo = categoryRepo;
        this.categoryMapper = categoryMapper;
        this.eventRepo = eventRepo;
    }

    @Override
    public CategoryOutDto addedCat(CategoryInDto categoryInDto) {
        if (categoryRepo.existsByName(categoryInDto.getName())) {
            throw new UnfulfilledConditionException(
                    String.format("The name %s already is used", categoryInDto.getName()));
        }
        Category category = categoryRepo.save(categoryMapper.mapToCategory(categoryInDto));
        log.info("The category has been successfully added, with id={} ", category.getId());
        return categoryMapper.toDto(category);
    }

    @Override
    public void deleteCat(Long catId) {
        if (eventRepo.existsByCategoryId(catId)) {
            throw new UnfulfilledConditionException("To delete a category, it should not be linked to an event");
        }

        if(!categoryRepo.existsById(catId)) {
            throw new UnfulfilledConditionException(String.format("Category with id=%s not found", catId));
        }

        categoryRepo.deleteById(catId);
        log.info("category id={} successfully deleted", catId);
    }

    @Override
    public CategoryOutDto updateCat(Long catId, CategoryInDto categoryInDto) {
        Category category = categoryRepo.findById(catId).orElseThrow(() -> new NotFoundException(
                String.format("Category with id=%s not found", catId)));

        if (categoryInDto.getName() != null && !categoryInDto.getName().equals(category.getName())) {
            if (categoryRepo.existsByName(categoryInDto.getName())) {
                throw new UnfulfilledConditionException(String.format("The name %s already is used", categoryInDto.getName()));
            }
        }

        Category updated = categoryMapper.mapToCategory(categoryInDto);
        updated.setId(catId);
        Category updatedCategory = categoryRepo.save(updated);
        log.info("Category id={}, name={} successfully updated",
                updatedCategory.getId(), updatedCategory.getName());
        return categoryMapper.toDto(updatedCategory);
    }

    @Override
    public List<CategoryOutDto> getCategories(int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        return categoryRepo.findAll(pageable).stream().map(categoryMapper::toDto).toList();
    }

    @Override
    public CategoryOutDto getCategory(Long catId) {
        Category foundCategory = categoryRepo.findById(catId).orElseThrow(() -> new NotFoundException(
                String.format("The category with id=%s not found", catId)));
        return categoryMapper.toDto(foundCategory);
    }
}
