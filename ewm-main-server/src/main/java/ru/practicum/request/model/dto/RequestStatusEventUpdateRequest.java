package ru.practicum.request.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.enums.Status;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestStatusEventUpdateRequest {
    private List<Long> requestIds;
    private Status status;
}
