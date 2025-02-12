package ru.practicum.user.dto;

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

    @Email(message = "The email is incorrect")
    private String email;

    @NotBlank(message = "Name cannot be missing")
    @Size(min = 2, max = 256, message = "The name size should be in the range from 2 to 256 characters." )
    private String name;

}
