package org.example.bereal.repository;

import org.example.bereal.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface BerealRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserIdAndPostedAtBetween(Long userId, LocalDateTime startOfDay, LocalDateTime endOfDay);
    List<Post> findByPostedAtBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
}
