package ru.yandex.practicum.filmorate.controller;

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
    public ResponseEntity<Film> create(Film film) {
        film.setId(idCounter++);
        films.add(film);
        log.info("Добавлен фильм: {}", film.getName());
        return ResponseEntity.ok(film);
    }

    @PutMapping
    public ResponseEntity<Film> update(Film film) {
        boolean found = false;
        for (int i = 0; i < films.size(); i++) {
            if (films.get(i).getId().equals(film.getId())) {
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



