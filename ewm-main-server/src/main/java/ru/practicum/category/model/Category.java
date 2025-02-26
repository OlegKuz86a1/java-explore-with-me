package ru.practicum.category.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.practicum.common.BaseEntity;


@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

    private String name;
}
