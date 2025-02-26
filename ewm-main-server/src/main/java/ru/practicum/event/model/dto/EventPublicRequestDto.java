package ru.practicum.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventPublicRequestDto {

    @Size(min = 20, max = 7000, message = "Description size should be in the range from 20 to 7000 characters")
    private String text;
    private List<Long> categories;
    private Boolean paid;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeStart;

    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeEnd;
    private Boolean onlyAvailable = false;
    private String sort;

    @PositiveOrZero
    private Integer from = 0;

    @Positive
    private Integer size = 10;
}
