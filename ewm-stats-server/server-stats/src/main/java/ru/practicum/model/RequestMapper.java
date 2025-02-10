package ru.practicum.model;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.practicum.RequestHitsOutDto;
import ru.practicum.RequestInDto;
import ru.practicum.common.EntityMapper;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.ERROR)
public abstract class RequestMapper implements EntityMapper<RequestInDto, Request> {

    public RequestMapper() {
    }

    public abstract RequestHitsOutDto mapToRequestDto(RequestHits requestHits);
}