package ru.practicum.tools.date;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class DateTimeTools implements ConstraintValidator<DifferenceFromCurrentTime, LocalDateTime> {
    private LocalDateTime minDateTime;
    private boolean isNullable;

    @Override
    public void initialize(DifferenceFromCurrentTime constraintAnnotation) {
        int days = constraintAnnotation.days();
        int hours = constraintAnnotation.hours();
        int minutes = constraintAnnotation.minutes();
        isNullable = constraintAnnotation.isNullable();
        minDateTime = LocalDateTime.now().plusDays(days).plusHours(hours).plusMinutes(minutes);
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext constraintValidatorContext) {
        if (isNullable && value == null) {
            return true;
        }

        assert value != null;
        return value.isAfter(minDateTime);

    }
}
