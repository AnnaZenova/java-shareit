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

    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    List<Booking> findByItemOwnerIdOrderByStartDesc(Long ownerId);

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
    Optional<Booking> findFirstByItemIdAndEndBeforeAndStatusOrderByEndDesc(@Param("itemId") Long itemId,
                                                                           @Param("endDateTime") LocalDateTime endDateTime,
                                                                           @Param("status") Status status);
}