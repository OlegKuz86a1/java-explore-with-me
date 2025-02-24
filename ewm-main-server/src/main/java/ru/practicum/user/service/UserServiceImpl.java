package ru.practicum.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.common.exception.UnfulfilledConditionException;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.dto.UserInDto;
import ru.practicum.user.model.dto.UserOutDto;
import ru.practicum.user.model.User;
import ru.practicum.user.model.UserMapper;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public UserOutDto addUser(UserInDto userInDto) {
        if (userRepository.existsByEmail(userInDto.getEmail())) {
            throw new UnfulfilledConditionException(String.format("The email %s already is used", userInDto.getEmail()));
        }

        User saveUser = userRepository.save(userMapper.toEntity(userInDto));
        log.info("New user with name {} and email {}, has been successfully added, with id={} ",
                                                            saveUser.getName(), saveUser.getEmail(), saveUser.getId());
        return userMapper.MapToUserOutDto(saveUser);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserOutDto> getUsers(List<Long> ids, int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        List<User> users = ids != null ? userRepository.findByIdIn(ids, pageable).getContent()
                : userRepository.findAll(pageable).getContent();

        return users.stream()
                .map(userMapper::MapToUserOutDto)
                .toList();
    }

    @Transactional
    @Override
    public void deleteUser(Long id) { // в тестах Постман вызов делит метода сделан с отсрочкой 60, нужно настроить транзакцию, что бы метод get сработал раньше чем delete.
        if(!userRepository.existsById(id)) {
            throw new NotFoundException(String.format("User with id=%s not found", id));
        }
        userRepository.deleteById(id);
        log.info("the user with the id {}, has been deleted", id);
    }
}
