package ru.practicum.category.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryInDto {

    @NotBlank(message = "The name the category cannot be missing")
    @Size(min = 1, max = 50, message = "The name the category size should be in the range from 1 to 50 characters.")
    private String name;
}
