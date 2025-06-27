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
    Booking save(Booking booking);

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

    boolean existsByBookerIdAndItemIdAndEndBefore(Long bookerId, Long itemId, LocalDateTime end);

    Optional<Booking> findFirstByItemIdAndStartAfterAndStatusIn(
            Long itemId,
            LocalDateTime start,
            List<Status> statuses
    );

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.id = :itemId " +
            "AND b.end < :endDateTime " +
            "AND b.status = :status " +
            "ORDER BY b.end DESC")
    Optional<Booking> findFirstByItemIdAndEndBeforeAndStatusOrderByEndDesc(
            @Param("itemId") Long itemId,
            @Param("endDateTime") LocalDateTime endDateTime,
            @Param("status") Status status);
}