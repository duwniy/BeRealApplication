package org.example.bereal.repository;

import org.example.bereal.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUserIdAndPostedAtBetween(Long userId, LocalDateTime startOfDay, LocalDateTime endOfDay);

    List<Post> findByPostedAtBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);

    /**
     * Находит публичные посты
     */
    @Query("SELECT p FROM Post p WHERE p.visibility = 'PUBLIC' ORDER BY p.postedAt DESC")
    Page<Post> findPublicPosts(Pageable pageable);

    /**
     * Находит посты пользователя и публичные посты
     */
    @Query("SELECT p FROM Post p WHERE p.userId = :userId OR p.visibility = 'PUBLIC' ORDER BY p.postedAt DESC")
    Page<Post> findUserAndPublicPosts(@Param("userId") Long userId, Pageable pageable);

    /**
     * Находит посты видимые для пользователя (свои + друзей + публичные)
     */
    @Query("SELECT p FROM Post p WHERE " +
            "p.userId = :userId OR " +
            "p.visibility = 'PUBLIC' OR " +
            "(p.visibility = 'FRIENDS_ONLY' AND p.userId IN :friendIds) " +
            "ORDER BY p.postedAt DESC")
    Page<Post> findVisiblePosts(@Param("userId") Long userId,
                                @Param("friendIds") List<Long> friendIds,
                                Pageable pageable);
}