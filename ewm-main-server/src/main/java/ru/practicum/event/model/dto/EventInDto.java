package ru.practicum.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.tools.date.DifferenceFromCurrentTime;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class EventInDto {

    @NotBlank(message = "Annotation not be blank")
    @Size(min = 20, max = 2000, message = "Annotation size should be in the range from 20 to 2000 characters")
    private String annotation;

    @NotNull(message = "Category not be blank")
    private Long category;

    @NotNull(message = "Location not be blank")
    private LocationDto location;

    @NotNull(message = "Title not be blank")
    @Size(min = 3, max = 120, message = "Title size should be in the range from 3 to 120 characters")
    private String title;

    @NotBlank(message = "Description not be blank")
    @Size(min = 20, max = 7000, message = "Description size should be in the range from 20 to 7000 characters")
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DifferenceFromCurrentTime(
            message = "The event is scheduled to take place 2 hours from the current date.",
            hours = 2
    )
    private LocalDateTime eventDate;
    private Boolean paid;

    @PositiveOrZero
    private Integer participantLimit;
    private Boolean requestModeration;
}
