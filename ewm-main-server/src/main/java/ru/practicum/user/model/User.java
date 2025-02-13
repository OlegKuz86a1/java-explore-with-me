package ru.practicum.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.practicum.common.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Column(length = 128, unique = true, nullable = false)
    private String email;
    private String name;

}
