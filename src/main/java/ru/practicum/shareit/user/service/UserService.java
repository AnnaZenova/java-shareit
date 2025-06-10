package ru.practicum.shareit.user.service;

import java.util.Collection;
import ru.practicum.shareit.user.model.User;

public interface UserService {
    Collection<User> getAllUsers();

    User addUser(User user);

    User getUserById(long id);

    User updateUser(long userId, User updatedUser);

    long removeUserById(long id);

    User getUserEntity(Long userId);
}