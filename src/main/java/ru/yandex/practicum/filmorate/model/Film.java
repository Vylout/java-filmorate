package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Duration;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class Film {

    private int id;
    @NonNull
    private String name;
    private String description;
    @NonNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @NonNull
    private int duration;
}
