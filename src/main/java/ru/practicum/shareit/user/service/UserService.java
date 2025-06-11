package ru.practicum.shareit.user.service;

import java.util.Collection;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public interface UserService {
    Collection<User> getAllUsers();

    UserDto addUser(UserDto userDto);

    UserDto updateUser(Long userId, UserDto userDto);

    UserDto getUserById(Long userId);

    long removeUserById(long id);

    User getUserEntity(Long userId);
}