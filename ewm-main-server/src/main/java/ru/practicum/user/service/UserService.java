package ru.practicum.user.service;

import ru.practicum.user.model.dto.UserInDto;
import ru.practicum.user.model.dto.UserOutDto;

import java.util.List;

public interface UserService {

    UserOutDto addUser(UserInDto userInDto);

    List<UserOutDto> getUsers(List<Long> ids, int from, int size);

    void deleteUser(Long id);
}
