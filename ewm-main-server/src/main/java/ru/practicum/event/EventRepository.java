package ru.practicum.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.practicum.event.model.Event;

import java.util.Optional;

@EnableJpaRepositories
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    boolean existsByCategoryId(Long catId);

   Optional<Event> findByInitiatorIdAndId(Long userId, Long eventId);

    boolean existsByIdAndInitiatorId(long eventId, long userId);
}
