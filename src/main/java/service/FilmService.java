package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final Map<Long, Set<Long>> filmLikes = new HashMap<>();

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film create(Film film) {
        if (film == null) {
            throw new ValidationException("Фильм не может быть null");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(Film.FIRST_FILM_DATE)) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        if (film == null) {
            throw new ValidationException("Фильм не может быть null");
        }
        if (film.getId() == null) {
            throw new ValidationException("Id фильма не задан");
        }
        Film existing = filmStorage.findById(film.getId());
        if (existing == null) {
            throw new NotFoundException("Фильм не найден");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(Film.FIRST_FILM_DATE)) {
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
                .sorted((a, b) -> {
                    long likesA = filmLikes.getOrDefault(a.getId(), Set.of()).size();
                    long likesB = filmLikes.getOrDefault(b.getId(), Set.of()).size();
                    return Long.compare(likesB, likesA); // убывание
                })
                .limit(safeCount)
                .collect(Collectors.toList());
    }

    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.findById(filmId);
        userStorage.findById(userId);
        filmLikes.computeIfAbsent(filmId, k -> new HashSet<>()).add(userId);
    }

    public void removeLike(Long filmId, Long userId) {
        Film film = filmStorage.findById(filmId);
        userStorage.findById(userId);
        filmLikes.computeIfPresent(filmId, (k, v) -> {
            v.remove(userId);
            return v.isEmpty() ? null : v; // убираем пустые записи
        });
    }
}






