package ru.practicum.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Page<User> findByIdIn(List<Long> id, Pageable pageable);
}
