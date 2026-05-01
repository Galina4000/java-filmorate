package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final Map<Long, Set<Long>> userFriends = new HashMap<>();

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.create(user);
    }

    public User update(User user) {
        User existing = userStorage.findById(user.getId());
        if (existing == null) {
            throw new ValidationException("Пользователь не найден");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.update(user);
    }

    public User findById(Long id) {
        return userStorage.findById(id);
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public void addFriend(Long userId, Long friendId) {
        User user = userStorage.findById(userId);
        User friend = userStorage.findById(friendId);
        if (user == null || friend == null) {
            throw new ValidationException("Пользователь не найден");
        }
        userFriends.computeIfAbsent(userId, k -> new HashSet<>()).add(friendId);
        userFriends.computeIfAbsent(friendId, k -> new HashSet<>()).add(userId);
    }

    public void removeFriend(Long userId, Long friendId) {
        userFriends.computeIfPresent(userId, (k, v) -> {
            v.remove(friendId);
            return v;
        });
        userFriends.computeIfPresent(friendId, (k, v) -> {
            v.remove(userId);
            return v;
        });
    }

    public List<User> getFriends(Long userId) {
        Set<Long> friendIds = userFriends.getOrDefault(userId, Set.of());
        return friendIds.stream()
                .map(userStorage::findById)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        Set<Long> userFriendsSet = userFriends.getOrDefault(userId, Set.of());
        Set<Long> otherFriendsSet = userFriends.getOrDefault(otherId, Set.of());
        Set<Long> common = userFriendsSet.stream()
                .filter(otherFriendsSet::contains)
                .collect(Collectors.toSet());
        return common.stream()
                .map(userStorage::findById)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}


