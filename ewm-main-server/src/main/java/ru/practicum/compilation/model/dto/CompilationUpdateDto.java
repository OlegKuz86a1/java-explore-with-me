package ru.practicum.compilation.model.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompilationUpdateDto {

    private List<Long> events;
    private Boolean pinned;

    @Size(min = 1,
            max = 50,
            message = "Title size should be in the range from 1 to 50 characters")
    private String title;
}
