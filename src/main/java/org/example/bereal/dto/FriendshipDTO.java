package org.example.bereal.dto;

import java.time.LocalDateTime;

public record FriendshipDTO(
        Long id,
        Long userId,
        Long friendId,
        String status,
        LocalDateTime createdAt,
        LocalDateTime acceptedAt
) {}