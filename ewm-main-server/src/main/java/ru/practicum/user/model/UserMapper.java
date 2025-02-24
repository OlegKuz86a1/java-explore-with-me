package ru.practicum.user.model;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.practicum.common.EntityMapper;
import ru.practicum.user.model.dto.UserInDto;
import ru.practicum.user.model.dto.UserOutDto;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public abstract class UserMapper implements EntityMapper<UserInDto, User> {

    public abstract UserOutDto MapToUserOutDto(User user);

}
