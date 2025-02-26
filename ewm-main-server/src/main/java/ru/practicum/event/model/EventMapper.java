package ru.practicum.event.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.practicum.common.EntityMapper;
import ru.practicum.event.model.dto.EventFullDto;
import ru.practicum.event.model.dto.EventInDto;
import ru.practicum.event.model.dto.EventOutDto;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public abstract class EventMapper implements EntityMapper<EventOutDto, Event> {

    @Mapping(target = "category",
            expression = "java(ru.practicum.category.model.Category.builder().id(eventInDto.getCategory()).build())")
    @Mapping(target = "lat", source = "location.lat")
    @Mapping(target = "lon", source = "location.lon")
    public abstract Event mapInDtoToEvent(EventInDto eventInDto);

    @Mapping(target = "location",
            expression = "java(ru.practicum.event.model.dto.LocationDto.builder().lat(event.getLat()).lon(event.getLon()).build())")
    public abstract EventFullDto mapEventToFullDto(Event event);
}
