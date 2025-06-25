package ru.practicum.shareit.booking.repository;

import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Status;
import java.time.LocalDateTime;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    //Заменяем save
    @Override
    <S extends Booking> S save(S entity);

    //Заменяем findById()
    @Override
    Optional<Booking> findById(Long id);

    // Бронирования пользователя, заменяем  List<Booking> findByBookerId(Long bookerId)
    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    // Бронирования владельца вещей, заменяем List<Booking> findByItemOwnerId(Long ownerId)
    List<Booking> findByItemOwnerIdOrderByStartDesc(Long ownerId);

    // Бронирования конкретной вещи
    List<Booking> findByItemIdOrderByStartDesc(Long itemId);

    //Заменяем void deleteById(Long id);
    @Override
    void deleteById(Long id);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, Status status);

    //Заменяем List<Booking> findByItemId(Long itemId);
    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId, Status status);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.id = :itemId " +
            "AND b.start < :now " +
            "AND b.status = :status " +
            "ORDER BY b.start DESC")
    List<Booking> findLastBookingsForItem(
            @Param("itemId") Long itemId,
            @Param("now") LocalDateTime now,
            @Param("status") Status status);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.id = :itemId " +
            "AND b.start > :now " +
            "AND b.status = :status " +
            "ORDER BY b.start ASC")
    List<Booking> findNextBookingsForItem(
            @Param("itemId") Long itemId,
            @Param("now") LocalDateTime now,
            @Param("status") Status status);

    List<Booking> findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
            Long bookerId, LocalDateTime start, LocalDateTime end);

    List<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(Long bookerId, LocalDateTime end);

    List<Booking> findByBookerIdAndStartAfterOrderByStartDesc(Long bookerId, LocalDateTime start);

    List<Booking> findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(
            Long ownerId, LocalDateTime start, LocalDateTime end);

    List<Booking> findByItemOwnerIdAndEndBeforeOrderByStartDesc(Long ownerId, LocalDateTime end);

    List<Booking> findByItemOwnerIdAndStartAfterOrderByStartDesc(Long ownerId, LocalDateTime start);

    boolean existsByBookerIdAndItemIdAndEndBefore(Long bookerId, Long itemId, LocalDateTime end);

    Optional<Booking> findFirstByItemIdAndEndBeforeOrderByEndDesc(
            Long itemId,
            LocalDateTime endBefore
    );

    Optional<Booking> findFirstByItemIdAndEndBeforeAndStatusIn(
            Long itemId,
            LocalDateTime end,
            List<Status> statuses
    );

    Optional<Booking> findFirstByItemIdAndStartAfterAndStatusIn(
            Long itemId,
            LocalDateTime start,
            List<Status> statuses
    );
}