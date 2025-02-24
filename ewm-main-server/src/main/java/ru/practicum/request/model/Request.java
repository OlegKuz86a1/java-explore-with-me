package ru.practicum.request.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.common.BaseEntity;
import ru.practicum.enums.Status;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "event_request")
public class Request extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @CreationTimestamp
    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "requester", nullable = false)
    private User requester;

    @Enumerated(EnumType.STRING)
    private Status status;

}
