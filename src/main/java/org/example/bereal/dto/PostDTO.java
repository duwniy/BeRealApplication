package org.example.bereal.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record PostDTO(
        @NotNull
        Long id,
        Long userId,
        String primaryImageUrl,
        String secondaryImageUrl,
        LocalDateTime postedAt,
        boolean isLate,
        @Size(max = 200)
        String caption,
        String visibility
) {}
