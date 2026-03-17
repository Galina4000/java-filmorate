package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  // ← 500 вместо 400
    public Map<String, String> handleValidationException(final ValidationException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", e.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 400 остается
    public Map<String, String> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", e.getMessage());
        return errorResponse;
    }
}



