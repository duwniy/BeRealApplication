package org.example.bereal.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "friendships", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "friend_id"})
})
@Data
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long friendId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FriendshipStatus status = FriendshipStatus.PENDING;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime acceptedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public enum FriendshipStatus {
        PENDING,    // Запрос отправлен
        ACCEPTED,   // Запрос принят
        REJECTED,   // Запрос отклонён
        BLOCKED     // Пользователь заблокирован
    }
}