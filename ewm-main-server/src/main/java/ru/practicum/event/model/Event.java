package ru.practicum.event.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.category.model.Category;
import ru.practicum.common.BaseEntity;
import ru.practicum.enums.Status;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

import static ru.practicum.enums.Status.PENDING;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
public class Event extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @Column(name = "created")
    @CreationTimestamp
    private LocalDateTime created;
    private String description;
    private String annotation;
    private String title;
    private Boolean paid;
    private LocalDateTime published;
    private Double lat;
    private Double lon;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @PrePersist
    @PreUpdate
    private void handleDefaultValue() {
        if (paid == null) {
            paid = false;
        }
        if (requestModeration == null) {
            requestModeration = true;
        }
        if (participantLimit == null) {
            participantLimit = 0;
        }
        if (status == null) {
            status = PENDING;
        }
    }
}
