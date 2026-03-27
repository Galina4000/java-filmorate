package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final List<Film> films = new ArrayList<>();
    private Long idCounter = 1L;

    @GetMapping
    public List<Film> getAll() {
        return films;
    }

    @PostMapping
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(Film.FIRST_FILM_DATE)) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        film.setId(idCounter++);
        films.add(film);
        log.info("Добавлен фильм: {}", film.getName());
        return ResponseEntity.ok(film);
    }

    @PutMapping
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) {
        boolean found = false;
        for (int i = 0; i < films.size(); i++) {
            if (films.get(i).getId().equals(film.getId())) {
                if (film.getReleaseDate().isBefore(Film.FIRST_FILM_DATE)) {
                    throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
                }
                films.set(i, film);
                log.info("Обновлен фильм: {}", film.getName());
                found = true;
                break;
            }
        }
        if (!found) {
            log.warn("Фильм с id={} не найден для обновления", film.getId());
            throw new ValidationException("Фильм не найден");
        }
        return ResponseEntity.ok(film);
    }
}





