package org.example.bereal.mapper;

import org.example.bereal.dto.FriendshipDTO;
import org.example.bereal.model.Friendship;

public class FriendshipMapper {

    public static FriendshipDTO toDto(Friendship friendship) {
        return new FriendshipDTO(
                friendship.getId(),
                friendship.getUserId(),
                friendship.getFriendId(),
                friendship.getStatus().name(),
                friendship.getCreatedAt(),
                friendship.getAcceptedAt()
        );
    }
}