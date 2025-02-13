package ru.practicum.admin.service;

import ru.practicum.user.dto.UserInDto;
import ru.practicum.user.dto.UserOutDto;

import java.util.List;

public interface UserService {

    UserOutDto addUser(UserInDto userInDto);

    List<UserOutDto> getUsers(List<Long> id, int from, int size);

    void deleteUser(Long id);
}
