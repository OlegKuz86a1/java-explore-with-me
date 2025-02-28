package ru.practicum.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import ru.practicum.comment.model.Comment;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByAuthorIdAndId(Long userId, Long comId);

    List<Comment> findAllByAuthorId(Long userId);

    Page<Comment> findAllByEventId(Long eventId, Pageable pageable);

    @Query(value = "select c from Comment c where lower(c.content) like lower(concat('%', :content, '%'))")
    Page<Comment> findCommentsByContent(@Param("content") String content, Pageable pageable);
}
