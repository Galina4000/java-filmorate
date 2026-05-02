package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final Map<Long, Set<Long>> filmLikes = new HashMap<>();

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film create(Film film) {
        if (film.getReleaseDate().isBefore(Film.FIRST_FILM_DATE)) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        if (film.getId() == null) {
            throw new ValidationException("Id фильма не задан");
        }
        Film existing = filmStorage.findById(film.getId());
        if (existing == null) {
            throw new NotFoundException("Фильм не найден");
        }
        if (film.getReleaseDate().isBefore(Film.FIRST_FILM_DATE)) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        return filmStorage.update(film);
    }

    public Film findById(Long id) {
        Film film = filmStorage.findById(id);
        if (film == null) {
            throw new NotFoundException("Фильм не найден");
        }
        return film;
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public List<Film> getPopular(int count) {
        int safeCount = Math.max(0, count);
        return filmStorage.findAll().stream()
                .sorted((a, b) -> Long.compare(
                        filmLikes.getOrDefault(b.getId(), Set.of()).size(),
                        filmLikes.getOrDefault(a.getId(), Set.of()).size()
                ))
                .limit(safeCount)
                .collect(Collectors.toList());
    }

    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.findById(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм не найден");
        }
        filmLikes.computeIfAbsent(filmId, k -> new HashSet<>()).add(userId);
    }

    public void removeLike(Long filmId, Long userId) {
        if (!filmLikes.containsKey(filmId)) {
            throw new NotFoundException("Фильм не найден");
        }
        Set<Long> likes = filmLikes.get(filmId);
        likes.remove(userId);
    }
}


