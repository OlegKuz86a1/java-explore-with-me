package ru.practicum.user.model;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.practicum.common.EntityMapper;
import ru.practicum.user.dto.UserInDto;
import ru.practicum.user.dto.UserOutDto;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.ERROR)
public abstract class UserMapper implements EntityMapper<UserInDto, User> {

    public abstract UserOutDto MapToUserOutDto(User user);

}
