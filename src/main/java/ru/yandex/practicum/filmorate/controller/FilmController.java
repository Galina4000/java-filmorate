package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmService filmService;
    private final UserService userService;

    public FilmController(FilmService filmService, UserService userService) {
        this.filmService = filmService;
        this.userService = userService;
    }

    @GetMapping
    public List<Film> getAll() {
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable Long id) {
        return filmService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) {
        Film created = filmService.create(film);
        log.info("Добавлен фильм: {}", created.getName());
        return ResponseEntity.ok(created);
    }

    @PutMapping
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) {
        Film updated = filmService.update(film);
        log.info("Обновлен фильм: {}", updated.getName());
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> addLike(
            @PathVariable Long id,
            @PathVariable Long userId
    ) {
        // Эти две строки обеспечивают 404 при несуществующих id
        filmService.findById(id);
        userService.findById(userId);
        filmService.addLike(id, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> removeLike(
            @PathVariable Long id,
            @PathVariable Long userId
    ) {
        // Эти две строки обеспечивают 404 при несуществующих id
        filmService.findById(id);
        userService.findById(userId);
        filmService.removeLike(id, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopular(count);
    }
}












