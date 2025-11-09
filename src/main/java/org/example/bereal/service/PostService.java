package org.example.bereal.service;

import org.example.bereal.exception.PostNotFoundException;
import org.example.bereal.exception.UnauthorizedException;
import org.example.bereal.exception.UserAlreadyPostedException;
import org.example.bereal.model.Post;
import org.example.bereal.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private static final Logger log = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final BeRealTimeService beRealTimeService;
    private final PostVisibilityChecker visibilityChecker;

    public PostService(PostRepository postRepository,
                       BeRealTimeService beRealTimeService,
                       PostVisibilityChecker visibilityChecker) {
        this.postRepository = postRepository;
        this.beRealTimeService = beRealTimeService;
        this.visibilityChecker = visibilityChecker;
    }

    /**
     * Создаёт новый пост с проверкой "один пост в день"
     */
    @Transactional
    public Post createPost(Post post) {
        log.info("Creating post for user: {}", post.getUserId());

        if (post.getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        // Проверка: уже постил сегодня?
        if (hasUserPostedToday(post.getUserId())) {
            throw new UserAlreadyPostedException("You have already posted today. Come back tomorrow!");
        }

        // Проверка на опоздание
        LocalDateTime todayBeRealTime = beRealTimeService.getTodayBeRealTime();
        LocalDateTime now = LocalDateTime.now();
        post.setLate(now.isAfter(todayBeRealTime.plusMinutes(2)));

        log.info("Post late status: {} (BeReal time: {}, posted at: {})",
                post.isLate(), todayBeRealTime, now);

        return postRepository.save(post);
    }

    /**
     * Проверяет, публиковал ли пользователь пост сегодня
     */
    private boolean hasUserPostedToday(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(LocalTime.MAX);

        List<Post> todayPosts = postRepository.findByUserIdAndPostedAtBetween(userId, start, end);
        return !todayPosts.isEmpty();
    }

    /**
     * Получает пост по ID
     */
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    /**
     * Получает пост по ID с проверкой видимости
     */
    public Optional<Post> getVisiblePostById(Long id, Long currentUserId) {
        return postRepository.findById(id)
                .filter(post -> visibilityChecker.isPostVisibleToUser(post, currentUserId));
    }

    /**
     * Получает все посты (без фильтрации, только для админов)
     */
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    /**
     * Получает видимые для пользователя посты (без пагинации)
     */
    public List<Post> getVisiblePosts(Long currentUserId) {
        List<Post> allPosts = postRepository.findAll();
        return visibilityChecker.filterVisiblePosts(allPosts, currentUserId);
    }

    public Page<Post> getVisiblePostsPaginated(Long currentUserId, Pageable pageable) {
        List<Long> friendIds = visibilityChecker.getFriendIds(currentUserId);

        // Если нет друзей, передаём пустой список (чтобы избежать SQL ошибки)
        if (friendIds.isEmpty()) {
            friendIds = List.of(-1L); // Несуществующий ID
        }

        return postRepository.findVisiblePosts(currentUserId, friendIds, pageable);
    }

    /**
     * Получает все посты конкретного пользователя
     */
    public List<Post> getUserPosts(Long userId) {
        return postRepository.findAll().stream()
                .filter(post -> post.getUserId().equals(userId))
                .toList();
    }

    /**
     * Обновляет существующий пост
     */
    @Transactional
    public Post updatePost(Long id, Post newPost) {
        return postRepository.findById(id)
                .map(existing -> {
                    // Проверяем, что пользователь обновляет свой пост
                    Long currentUserId = getCurrentUserId();
                    if (!existing.getUserId().equals(currentUserId)) {
                        throw new UnauthorizedException("You can only update your own posts");
                    }

                    // Обновляем только разрешённые поля
                    if (newPost.getPrimaryImageUrl() != null) {
                        existing.setPrimaryImageUrl(newPost.getPrimaryImageUrl());
                    }
                    if (newPost.getSecondaryImageUrl() != null) {
                        existing.setSecondaryImageUrl(newPost.getSecondaryImageUrl());
                    }
                    if (newPost.getCaption() != null) {
                        existing.setCaption(newPost.getCaption());
                    }
                    if (newPost.getVisibility() != null) {
                        existing.setVisibility(newPost.getVisibility());
                    }

                    log.info("Updated post {} by user {}", id, currentUserId);
                    return postRepository.save(existing);
                })
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    /**
     * Удаляет пост
     */
    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        Long currentUserId = getCurrentUserId();
        if (!post.getUserId().equals(currentUserId)) {
            throw new UnauthorizedException("You can only delete your own posts");
        }

        log.info("Deleting post {} by user {}", id, currentUserId);
        postRepository.deleteById(id);
    }

    /**
     * Получает все посты, созданные сегодня
     */
    public List<Post> getTodayPosts() {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(LocalTime.MAX);
        return postRepository.findByPostedAtBetween(start, end);
    }

    /**
     * Получает посты друзей для текущего пользователя
     */
    public List<Post> getFriendsPosts(Long currentUserId) {
        List<Long> friendIds = visibilityChecker.getFriendIds(currentUserId);

        return postRepository.findAll().stream()
                .filter(post -> friendIds.contains(post.getUserId()))
                .toList();
    }

    /**
     * Получает ID текущего аутентифицированного пользователя
     */
    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new UnauthorizedException("User not authenticated");
        }
        try {
            return Long.parseLong(auth.getName());
        } catch (NumberFormatException e) {
            throw new UnauthorizedException("Invalid user ID format");
        }
    }
}