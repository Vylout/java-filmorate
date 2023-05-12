package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class User {
    private int id;
    @NotBlank(message = "Email не может быть пустым.")
    @Email(message = "Некорректный адрес электронной почты.")
    private String email;
    @NonNull
    private String login;
    private String name;
    @NonNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
}
