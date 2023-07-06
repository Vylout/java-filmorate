package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class Film {
    private int id;
    @NotBlank(message = "Не указана название фильма.")
    private String name;
    @Size(max = 200, message = "Максимальная длина описания - 200 символов.")
    private String description;
    @NonNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительной.")
    private int duration;
    private Set<Integer> likes;

    public Film() {
        this.likes = new HashSet<>();
    }
}
