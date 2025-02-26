package ru.practicum.compilation.model;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.practicum.common.BaseEntity;
import ru.practicum.event.model.Event;

import java.util.List;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "compilations")
public class Compilation extends BaseEntity {
    private String title;
    private Boolean pinned;

    @ManyToMany
    @JoinTable(
            name = "compilation_events",
            joinColumns = { @JoinColumn(name = "compilation_id") },
            inverseJoinColumns = { @JoinColumn(name = "event_id") }
    )
   private List<Event> events;

    @PrePersist
    @PreUpdate
    private void handleDefaultValue() {
        if (pinned == null) {
            pinned = false;
        }
    }
}
