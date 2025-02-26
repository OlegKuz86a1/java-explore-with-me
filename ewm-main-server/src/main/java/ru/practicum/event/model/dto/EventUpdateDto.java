package ru.practicum.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.enums.StateAction;
import ru.practicum.tools.date.DifferenceFromCurrentTime;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventUpdateDto {

    @Size(min = 20, max = 2000, message = "Annotation size should be in the range from 20 to 2000 characters")
    private String annotation;
    private Long category;

    private LocationDto location;

    @Size(min = 3, max = 120, message = "Title size should be in the range from 3 to 120 characters")
    private String title;

    @Size(min = 20, max = 7000, message = "Description size should be in the range from 20 to 7000 characters")
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DifferenceFromCurrentTime(
            message = "The event is scheduled to take place 2 hours from the current date.",
            hours = 2,
            isNullable = true
    )
    private LocalDateTime eventDate;
    private Boolean paid;

    @PositiveOrZero
    private Integer participantLimit;
    private Boolean requestModeration;

    @JsonProperty("stateAction")
    private StateAction state;


}
