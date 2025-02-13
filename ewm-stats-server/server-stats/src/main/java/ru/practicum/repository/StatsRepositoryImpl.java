package ru.practicum.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.RequestHits;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsRepositoryImpl implements StatsCustomRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public List<RequestHits> getRequestHits(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        String jpql = "SELECT r.app, r.uri, " + (unique ? "COUNT(DISTINCT r.ip) " : "COUNT(r.ip) ") + "as hits FROM Request r " + String.valueOf(getStringBuilder(start, end, uris)) + " GROUP BY r.uri, r.app ORDER BY hits DESC";
        TypedQuery<Object[]> query = this.entityManager.createQuery(jpql, Object[].class);
        if (uris != null && !uris.isEmpty()) {
            query.setParameter("uris", uris);
        }

        if (start != null) {
            query.setParameter("start", start);
        }

        if (end != null) {
            query.setParameter("end", end);
        }

        List<RequestHits> collect = query.getResultList().stream()
                .map((arr) -> new RequestHits(
                        (String)arr[0], //app
                        String.valueOf(Collections.singletonList((String)arr[1])), //uri
                        ((Number)arr[2]).longValue())) //hits
                .collect(Collectors.toList());
        System.out.println("REQUEST-HITS" + String.valueOf(collect));
        return collect;
    }

    private static StringBuilder getStringBuilder(LocalDateTime start, LocalDateTime end, List<String> uris) {
        StringBuilder condition = new StringBuilder();
        if (uris != null && !uris.isEmpty()) {
            condition.append("WHERE r.uri in ( :uris ) ");
        }

        if (start != null) {
            String whereStart = "r.timestamp > :start ";
            condition.append(condition.isEmpty() ? "WHERE " : "AND ").append(whereStart);
        }

        if (end != null) {
            String whereEnd = "r.timestamp < :end ";
            condition.append(condition.isEmpty() ? "WHERE " : "AND ").append(whereEnd);
        }

        return condition;
    }
}