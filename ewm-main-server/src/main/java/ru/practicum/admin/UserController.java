package ru.practicum.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.admin.service.UserServiceImpl;
import ru.practicum.user.dto.UserInDto;
import ru.practicum.user.dto.UserOutDto;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@Validated
@Slf4j
public class UserController {

    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserOutDto addUser(@RequestBody @Valid UserInDto userInDto) {

        return userService.addUser(userInDto);
    }

    @GetMapping
    public List<UserOutDto> getUsers(@RequestParam(required = false) List<Long> id,
                                    @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                    @RequestParam(defaultValue = "10") @Min(1) int size ) {
        return userService.getUsers(id, from, size);

    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable @Positive Long userId) {
        userService.deleteUser(userId);
    }

}
