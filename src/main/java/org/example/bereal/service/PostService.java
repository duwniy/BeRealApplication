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

    @Transactional
    public Post createPost(Post post) {
        log.info("Creating post for user: {}", post.getUserId());

        if (post.getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        if (hasUserPostedToday(post.getUserId())) {
            throw new UserAlreadyPostedException("You have already posted today. Come back tomorrow!");
        }

        LocalDateTime todayBeRealTime = beRealTimeService.getTodayBeRealTime();
        LocalDateTime now = LocalDateTime.now();
        post.setLate(now.isAfter(todayBeRealTime.plusMinutes(2)));

        log.info("Post late status: {} (BeReal time: {}, posted at: {})",
                post.isLate(), todayBeRealTime, now);

        return postRepository.save(post);
    }

    private boolean hasUserPostedToday(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(LocalTime.MAX);

        List<Post> todayPosts = postRepository.findByUserIdAndPostedAtBetween(userId, start, end);
        return !todayPosts.isEmpty();
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public Optional<Post> getVisiblePostById(Long id, Long currentUserId) {
        return postRepository.findById(id)
                .filter(post -> visibilityChecker.isPostVisibleToUser(post, currentUserId));
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public List<Post> getVisiblePosts(Long currentUserId) {
        List<Post> allPosts = postRepository.findAll();
        return visibilityChecker.filterVisiblePosts(allPosts, currentUserId);
    }

    public Page<Post> getVisiblePostsPaginated(Long currentUserId, Pageable pageable) {
        Page<Post> allPosts = postRepository.findAll(pageable);
        return visibilityChecker.filterVisiblePosts(allPosts, currentUserId);
    }

    public List<Post> getUserPosts(Long userId) {
        return postRepository.findAll().stream()
                .filter(post -> post.getUserId().equals(userId))
                .toList();
    }

    @Transactional
    public Post updatePost(Long id, Post newPost, Long currentUserId) { // ← Добавляем currentUserId параметр
        return postRepository.findById(id)
                .map(existing -> {
                    if (!existing.getUserId().equals(currentUserId)) {
                        throw new UnauthorizedException("You can only update your own posts");
                    }

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

    @Transactional
    public void deletePost(Long id, Long currentUserId) { // ← Добавляем currentUserId параметр
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        if (!post.getUserId().equals(currentUserId)) {
            throw new UnauthorizedException("You can only delete your own posts");
        }

        log.info("Deleting post {} by user {}", id, currentUserId);
        postRepository.deleteById(id);
    }

    public List<Post> getTodayPosts() {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(LocalTime.MAX);
        return postRepository.findByPostedAtBetween(start, end);
    }

    public List<Post> getFriendsPosts(Long currentUserId) {
        List<Long> friendIds = visibilityChecker.getFriendIds(currentUserId);

        return postRepository.findAll().stream()
                .filter(post -> friendIds.contains(post.getUserId()))
                .toList();
    }
}