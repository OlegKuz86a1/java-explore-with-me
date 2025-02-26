package ru.practicum.user.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInDto {

    @NotBlank(message = "Email cannot be missing")
    @Email(message = "The email is incorrect")
    @Size(min = 6, max = 254, message = "The email size should be in the range from 4 to 254 characters.")
    private String email;

    @NotBlank(message = "Name cannot be missing")
    @Size(min = 2, max = 250, message = "The name size should be in the range from 2 to 250 characters.")
    private String name;
}
