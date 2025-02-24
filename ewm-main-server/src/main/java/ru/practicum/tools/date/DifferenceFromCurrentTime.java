package ru.practicum.tools.date;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Constraint(validatedBy = DateTimeTools.class)
public @interface DifferenceFromCurrentTime {
    String message() default "date and time verification conditions violated";
    int days() default 0;
    int hours() default 0;
    int minutes() default 0;
    boolean isNullable() default false;

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
