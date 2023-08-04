package ru.practicum.filmorate.film;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.*;
import java.time.Duration;
import java.time.LocalDate;

@Builder(toBuilder = true)
@Value
public class Film {
    int id;
    @NotNull @NotBlank
    String name;
    @Size(max = 200)
    String description;
    LocalDate releaseDate;
    @Positive
    Duration duration;
}
