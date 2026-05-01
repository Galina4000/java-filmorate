package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void filmValidDataTest() {
        Film film = new Film();
        film.setName("Валидный фильм");
        film.setDescription("Короткое описание");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(120);

        assertNotNull(film.getName());
        assertEquals("Короткое описание", film.getDescription());
    }

    @Test
    void userValidDataTest() {
        User user = new User();
        user.setEmail("test@test.ru");
        user.setLogin("validlogin");
        user.setBirthday(LocalDate.now().minusYears(20));

        assertEquals("test@test.ru", user.getEmail());
        assertEquals("validlogin", user.getLogin());

    }
}




