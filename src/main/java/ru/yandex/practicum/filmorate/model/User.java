package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class User {
    private Integer id;
    @NotBlank(message = "Email не может быть пустым.")
    @Email(message = "Некорректный адрес электронной почты.")
    private String email;
    @NotBlank(message = "Логин не может быть пустым.")
    @Pattern(regexp = "^\\S*$", message = "Логин не может содержать пробелы.")
    private String login;
    private String name;
    @NonNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "Указана не верная дата рождения.")
    private LocalDate birthday;
    private Set<Integer> friends;
    private Map<Integer, Boolean> friendStatus;
    private Set<Integer> likedFilms;

    public User() {
        this.friends = new HashSet<>();
        this.friendStatus = new HashMap<>();
        this.likedFilms = new HashSet<>();
    }

    public User(Integer id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.friends = new HashSet<>();
        this.friendStatus = new HashMap<>();
        this.likedFilms = new HashSet<>();
    }
}
