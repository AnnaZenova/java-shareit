package ru.practicum.shareit.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByItemId(Long itemId);

    List<Comment> findByItemIn(List<Item> items);

    @Query("SELECT c FROM Comment c JOIN FETCH c.author WHERE c.item.id = :itemId")
    List<Comment> findByItemIdWithAuthor(@Param("itemId") Long itemId);
}