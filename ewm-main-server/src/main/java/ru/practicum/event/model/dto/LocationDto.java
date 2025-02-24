package ru.practicum.event.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDto {

    @NotNull
    private Double lat;

    @NotNull
    private Double lon;

}
