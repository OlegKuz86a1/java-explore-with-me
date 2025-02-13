package ru.practicum.admin.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.common.NotFoundException;
import ru.practicum.user.UserRepository;
import ru.practicum.user.dto.UserInDto;
import ru.practicum.user.dto.UserOutDto;
import ru.practicum.user.model.User;
import ru.practicum.user.model.UserMapper;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserOutDto addUser(UserInDto userInDto) {

        User saveUser = userRepository.save(userMapper.toEntity(userInDto));
        log.info("New user with name {} and email {}, has been successfully added, with id={} ",
                                                            saveUser.getName(), saveUser.getEmail(), saveUser.getId());
        return userMapper.MapToUserOutDto(saveUser);
    }

    @Override
    public List<UserOutDto> getUsers(List<Long> id, int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        /* если понадобиться переводить int в Long
         List<Long> idLong = Arrays.stream(id)
                    .mapToObj(Long::valueOf)
                    .collect(Collectors.toList());
        */
        List<User> users = id != null ? userRepository.findByIdIn(id, pageable).getContent()
                : userRepository.findAll(pageable).getContent();


        return users.stream()
                .map(userMapper::MapToUserOutDto)
                .toList();
    }

    @Override
    public void deleteUser(Long id) {
        if(!userRepository.existsById(id)) {
            throw new NotFoundException(String.format("User with id=%s not found", id));
        }
        userRepository.deleteById(id);
        log.info("the user with the id {}, has been deleted", id);
    }


}
