package ru.practicum.filmorate.film;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Builder(toBuilder = true)
@Value
public class Film {
    int id;
    @NotNull @NotBlank
    String name;
    @Size(max = 200)
    String description;
    @NotNull
    LocalDate releaseDate;
    @NotNull @Positive
    int duration;
}
