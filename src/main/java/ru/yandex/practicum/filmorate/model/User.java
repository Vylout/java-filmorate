package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class User {
    private int id;
    @NonNull
    private String email;
    @NonNull
    private String login;
    private String name;
    @NonNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
}
