package org.example.bereal.controller;

import jakarta.validation.Valid;
import org.example.bereal.dto.PostDTO;
import org.example.bereal.mapper.PostMapper;
import org.example.bereal.model.Post;
import org.example.bereal.service.ImageService;
import org.example.bereal.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private static final Logger log = LoggerFactory.getLogger(PostController.class);

    private final PostService postService;
    private final ImageService imageService;

    public PostController(PostService postService, ImageService imageService) {
        this.postService = postService;
        this.imageService = imageService;
    }

    /**
     * Создать новый пост с изображениями
     */
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<PostDTO> createPost(
            @RequestParam("primaryImage") MultipartFile primaryImage,
            @RequestParam("secondaryImage") MultipartFile secondaryImage,
            @RequestParam(value = "caption", required = false) String caption,
            @RequestParam(value = "visibility", defaultValue = "PUBLIC") String visibility,
            Authentication authentication) throws IOException {

        Long userId = extractUserId(authentication);
        log.info("User {} creating new post", userId);

        // Загрузка изображений
        String primaryUrl = imageService.uploadImage(primaryImage, userId, "primary");
        String secondaryUrl = imageService.uploadImage(secondaryImage, userId, "secondary");

        // Создание поста
        Post post = new Post();
        post.setUserId(userId);
        post.setPrimaryImageUrl(primaryUrl);
        post.setSecondaryImageUrl(secondaryUrl);
        post.setCaption(caption);
        post.setVisibility(Post.Visibility.valueOf(visibility.toUpperCase()));

        Post created = postService.createPost(post);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PostMapper.toDto(created));
    }

    /**
     * Получить пост по ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPost(
            @PathVariable Long id,
            Authentication authentication) {

        Long currentUserId = extractUserId(authentication);

        return postService.getVisiblePostById(id, currentUserId)
                .map(PostMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Получить все видимые посты с пагинацией
     */
    @GetMapping
    public ResponseEntity<Page<PostDTO>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "postedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            Authentication authentication) {

        Long userId = extractUserId(authentication);

        Sort.Direction direction = sortDir.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<PostDTO> posts = postService.getVisiblePostsPaginated(userId, pageable)
                .map(PostMapper::toDto);

        return ResponseEntity.ok(posts);
    }

    /**
     * Получить посты друзей
     */
    @GetMapping("/friends")
    public ResponseEntity<List<PostDTO>> getFriendsPosts(Authentication authentication) {
        Long userId = extractUserId(authentication);

        List<PostDTO> posts = postService.getFriendsPosts(userId)
                .stream()
                .map(PostMapper::toDto)
                .toList();

        return ResponseEntity.ok(posts);
    }

    /**
     * Получить посты, созданные сегодня
     */
    @GetMapping("/today")
    public ResponseEntity<List<PostDTO>> getTodayPosts(Authentication authentication) {
        Long userId = extractUserId(authentication);

        List<PostDTO> posts = postService.getTodayPosts()
                .stream()
                .filter(post -> post.getUserId().equals(userId) ||
                        post.getVisibility() == Post.Visibility.PUBLIC)
                .map(PostMapper::toDto)
                .toList();

        return ResponseEntity.ok(posts);
    }

    /**
     * Получить посты конкретного пользователя
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostDTO>> getUserPosts(
            @PathVariable Long userId,
            Authentication authentication) {

        Long currentUserId = extractUserId(authentication);

        List<PostDTO> posts = postService.getUserPosts(userId)
                .stream()
                .filter(post -> {
                    // Показываем только видимые посты
                    if (post.getUserId().equals(currentUserId)) {
                        return true; // Свои посты всегда видны
                    }
                    return post.getVisibility() == Post.Visibility.PUBLIC;
                })
                .map(PostMapper::toDto)
                .toList();

        return ResponseEntity.ok(posts);
    }

    /**
     * Обновить пост (только caption и visibility)
     */
    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> updatePost(
            @PathVariable Long id,
            @RequestParam(value = "caption", required = false) String caption,
            @RequestParam(value = "visibility", required = false) String visibility) {

        Post updateData = new Post();
        updateData.setCaption(caption);

        if (visibility != null) {
            updateData.setVisibility(Post.Visibility.valueOf(visibility.toUpperCase()));
        }

        Post updated = postService.updatePost(id, updateData);
        return ResponseEntity.ok(PostMapper.toDto(updated));
    }

    /**
     * Удалить пост
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Извлекает ID пользователя из Authentication
     */
    private Long extractUserId(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new IllegalStateException("User not authenticated");
        }
        return Long.parseLong(authentication.getName());
    }
}