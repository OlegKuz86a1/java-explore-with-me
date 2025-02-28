package ru.practicum.comment.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentInDto {

    @NotBlank(message = "The text not be blank")
    @Size(min = 1, max = 2000, message = "The comment size should be in the range from 1 to 2000 characters")
    private String content;
}
