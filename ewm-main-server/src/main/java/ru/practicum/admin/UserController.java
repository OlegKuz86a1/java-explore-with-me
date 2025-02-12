package ru.practicum.admin;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.user.dto.UserInDto;
import ru.practicum.user.dto.UserOutDto;

@RestController
@RequestMapping
@Validated
@Slf4j
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    public UserOutDto addUser(@RequestBody @Valid UserInDto userInDto) {
        UserOutDto user = userService.addUser(userInDto);
        log.info("The user with name = {} and email = {}, has been successfully added", user.getName(), user.getEmail());
        return user;
    }

}
