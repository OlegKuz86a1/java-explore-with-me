package ru.practicum.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.enums.Status;
import ru.practicum.request.model.Request;

import java.util.List;

@EnableJpaRepositories
public interface RequestsRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequesterId(Long id);

    boolean existsByRequesterIdAndEventId(Long userId, Long eventId);

    int countByEventIdAndStatus(Long eventId, Status status);

    List<Request> findAllByEventId(long eventId);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Modifying
    @Query("update Request r set r.status = :status, r.created = CURRENT_TIMESTAMP " +
            "where r.id in :requestIds")
    void updateStatus(@Param("status") Status status, @Param("requestIds") List<Long> requestIds);

    List<Request> findAllByEventIdInAndStatus(List<Long> eventIds, Status status);

}
