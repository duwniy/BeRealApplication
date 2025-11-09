package org.example.bereal.mapper;

import org.example.bereal.dto.PostDTO;
import org.example.bereal.model.Post;

public class PostMapper {

    public static PostDTO toDto(Post post) {
        return new PostDTO(
                post.getId(),
                post.getUserId(),
                post.getPrimaryImageUrl(),
                post.getSecondaryImageUrl(),
                post.getPostedAt(),
                post.isLate(),
                post.getCaption(),
                post.getVisibility().name()
        );
    }

    public static Post fromDto(PostDTO dto) {
        Post post = new Post();
        post.setUserId(dto.userId());
        post.setPrimaryImageUrl(dto.primaryImageUrl());
        post.setSecondaryImageUrl(dto.secondaryImageUrl());
        post.setLate(dto.isLate());
        post.setCaption(dto.caption());

        if (dto.visibility() != null) {
            post.setVisibility(Post.Visibility.valueOf(dto.visibility()));
        }

        return post;
    }
}
