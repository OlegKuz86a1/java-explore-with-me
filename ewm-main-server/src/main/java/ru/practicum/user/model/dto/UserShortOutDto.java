package ru.practicum.user.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserShortOutDto {

    @NotBlank(message = "The user id not be blank")
    private Long id;

    @NotBlank(message = "The user name not be blank")
    private String name;
}
