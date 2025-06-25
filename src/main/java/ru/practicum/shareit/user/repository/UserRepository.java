package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;


import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    // Заменяем Collection<User> getAllUsers()
    @Override
    List<User> findAll();

    // Заменяем User addUser(User user);
    @Override
    <S extends User> S save(S entity);

    // Заменяем Optional<User> getUserById(long id);
    @Override
    Optional<User> findById(Long id);

    // User updateUser(User updatedUser); можно реализовать через save(), так как он обновляет при наличии ID
    default User updateUser(User updatedUser) {
        return save(updatedUser);
    }

    // Заменяем  long removeUserById(long id)
    @Override
    void deleteById(Long id);

    // Заменяем boolean checkUserById(long id)
    default boolean checkUserById(long id) {
        return existsById(id);
    }
}