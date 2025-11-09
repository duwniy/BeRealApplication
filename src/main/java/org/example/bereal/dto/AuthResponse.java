package org.example.bereal.dto;

public record AuthResponse(
        String token,
        Long userId,
        String username,
        String email
) {}