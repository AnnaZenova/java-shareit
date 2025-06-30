package ru.practicum.shareit.user.service;

import jakarta.validation.Valid;
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
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
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
    public UserDto addUser(@Valid UserDto userDto) {
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
    public UserDto updateUser(Long userId, @Valid UserDto updatedUserDto) {
        User user = UserMapper.toUser(getUserById(userId));
        if (updatedUserDto.getName() != null) {
            user.setName(updatedUserDto.getName());
        }
        if (updatedUserDto.getEmail() != null) {
            checkEmailAvailability(updatedUserDto.getEmail());
            user.setEmail(updatedUserDto.getEmail());
        }
        log.info("Обновлена пользователь с ID :{}", userId);
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void removeUserById(long id) {
        log.info("Удален пользователь с ID :{}", id);
    }

    private void checkValidEmailAddress(String email) {
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            throw new ValidationException(String.format("user.Email не в формате email."));
        }
    }

    private boolean checkEmailAvailability(String email) {
        checkValidEmailAddress(email);
        for (User u : getAllUsers()) {
            if (email.equals(u.getEmail())) {
                throw new EmailAlreadyExistsException(String.format("пользователь с таким email %s уже существует.",
                        email));
            }
        }
        return true;
    }

    @Override
    public User getUserEntity(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ElementNotFoundException("User не найден"));
    }
}