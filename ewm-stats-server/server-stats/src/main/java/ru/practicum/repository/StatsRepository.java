package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.practicum.model.Request;

@EnableJpaRepositories
public interface StatsRepository extends JpaRepository<Request, Long>, StatsCustomRepository {
}
