package ru.practicum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.practicum.common.BaseEntity;

import java.time.LocalDateTime;


@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "requests")
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Request extends BaseEntity {

    private String app;
    private String uri;
    private String ip;

    @Column(name = "request_date")
    private LocalDateTime timestamp;
}
