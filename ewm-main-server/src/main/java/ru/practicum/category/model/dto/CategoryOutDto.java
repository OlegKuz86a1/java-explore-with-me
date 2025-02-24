package ru.practicum.category.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryOutDto {
    private Long id;

    @NotBlank(message = "The name the category cannot be missing")
    @Size(min = 1, max = 50, message = "The name the category size should be in the range from 1 to 50 characters." )
    private String name;
}
