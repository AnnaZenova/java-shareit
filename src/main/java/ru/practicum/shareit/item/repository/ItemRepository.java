package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    // addItem заменяется стандартным save()
    @Override
    Item save(Item item);

    // getItemById заменяется стандартным findById()
    @Override
    Optional<Item> findById(Long id);

    // updateItem можно реализовать через save()
    default Item updateItem(Item item) {
        return save(item);
    }

    // Поиск всех предметов пользователя
    List<Item> findByOwnerId(Long userId);

    // Поиск доступных предметов по тексту
    @Query("SELECT i FROM Item i " +
            "WHERE i.available = true " +
            "AND (LOWER(i.name) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR LOWER(i.description) LIKE LOWER(CONCAT('%', :text, '%')))")
    List<Item> searchAvailableItems(@Param("text") String text);

    // getUserItems переименован в findByOwnerId для согласованности
    default Collection<Item> getUserItems(long userId) {
        return findByOwnerId(userId);
    }
}