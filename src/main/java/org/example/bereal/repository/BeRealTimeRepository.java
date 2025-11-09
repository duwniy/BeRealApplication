package org.example.bereal.repository;

import org.example.bereal.model.BeRealTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface BeRealTimeRepository extends JpaRepository<BeRealTime, Long> {
    Optional<BeRealTime> findTopByNotificationTimeBetweenOrderByNotificationTimeDesc(
            LocalDateTime start, LocalDateTime end
    );
}