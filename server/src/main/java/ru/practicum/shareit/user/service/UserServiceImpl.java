package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.ElementNotFoundException;
import ru.practicum.shareit.exceptions.EmailAlreadyExistsException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public UserDto addUser(UserDto userDto) {
        if (userDto.getId() != null) {
            throw new ValidationException("ID must be null for new user");
        }
        User user = UserMapper.toUser(userDto);
        checkEmailAvailability(user.getEmail());
        log.info("Добавлен пользователь :{}", userDto);
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public UserDto getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new ElementNotFoundException(String.format("Не найден пользователь с id%d.", id));
        }
        log.info("Получен впользователь с ID :{}", id);
        return UserMapper.toUserDto(optionalUser.get());
    }

    @Override
    @Transactional
    public UserDto updateUser(Long userId, UserDto updatedUserDto) {
        User user = UserMapper.toUser(getUserById(userId));

        if (updatedUserDto.getName() != null) {
            user.setName(updatedUserDto.getName());
        }

        if (updatedUserDto.getEmail() != null && !updatedUserDto.getEmail().equals(user.getEmail())) {
            checkEmailAvailability(updatedUserDto.getEmail());
            user.setEmail(updatedUserDto.getEmail());
        }

        log.info("Обновлен пользователь с ID: {}", userId);
        return UserMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public void removeUserById(long id) {
        log.info("Удален пользователь с ID :{}", id);
        userRepository.deleteById(id);
    }


    private void checkEmailAvailability(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
    }

    @Override
    public User getUserEntity(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ElementNotFoundException("User не найден"));
    }
}