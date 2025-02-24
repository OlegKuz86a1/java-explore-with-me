package ru.practicum.request.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestStatusEventUpdateResult {
    private List<RequestOutDto> confirmedRequests;
    private List<RequestOutDto> rejectedRequests;

}
