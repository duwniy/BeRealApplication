package org.example.bereal.service;

import org.example.bereal.model.Friendship;
import org.example.bereal.model.Post;
import org.example.bereal.repository.FriendshipRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Компонент для проверки видимости постов на основе настроек приватности
 * и статуса дружбы между пользователями
 */
@Component
public class PostVisibilityChecker {

    private final FriendshipRepository friendshipRepository;

    public PostVisibilityChecker(FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    /**
     * Проверяет, виден ли пост конкретному пользователю
     */
    public boolean isPostVisibleToUser(Post post, Long currentUserId) {
        // Свои посты всегда видны
        if (post.getUserId().equals(currentUserId)) {
            return true;
        }

        return switch (post.getVisibility()) {
            case PUBLIC -> true;
            case FRIENDS_ONLY -> areFriends(currentUserId, post.getUserId());
            case PRIVATE -> false;
        };
    }

    /**
     * Фильтрует список постов, оставляя только видимые для пользователя
     */
    public List<Post> filterVisiblePosts(List<Post> posts, Long currentUserId) {
        return posts.stream()
                .filter(post -> isPostVisibleToUser(post, currentUserId))
                .collect(Collectors.toList());
    }

    /**
     * Фильтрует Page постов, оставляя только видимые для пользователя
     */
    public Page<Post> filterVisiblePosts(Page<Post> postsPage, Long currentUserId) {
        List<Post> visiblePosts = postsPage.getContent().stream()
                .filter(post -> isPostVisibleToUser(post, currentUserId))
                .collect(Collectors.toList());

        return new PageImpl<>(visiblePosts, postsPage.getPageable(), postsPage.getTotalElements());
    }

    /**
     * Получает список ID друзей пользователя
     */
    public List<Long> getFriendIds(Long userId) {
        List<Friendship> friendships = friendshipRepository
                .findAllFriendships(userId, Friendship.FriendshipStatus.ACCEPTED);

        return friendships.stream()
                .map(f -> f.getUserId().equals(userId) ? f.getFriendId() : f.getUserId())
                .collect(Collectors.toList());
    }

    /**
     * Проверяет, являются ли два пользователя друзьями
     */
    public boolean areFriends(Long userId, Long friendId) {
        return friendshipRepository.areFriends(userId, friendId);
    }
}