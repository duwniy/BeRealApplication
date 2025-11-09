package org.example.bereal.controller;

import jakarta.validation.Valid;
import org.example.bereal.dto.PostDTO;
import org.example.bereal.mapper.PostMapper;
import org.example.bereal.model.Post;
import org.example.bereal.security.JwtUtil;
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
    private final JwtUtil jwtUtil; // ← Добавляем JwtUtil

    public PostController(PostService postService, ImageService imageService, JwtUtil jwtUtil) {
        this.postService = postService;
        this.imageService = imageService;
        this.jwtUtil = jwtUtil; // ← Инжектим JwtUtil
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<PostDTO> createPost(
            @RequestParam("primaryImage") MultipartFile primaryImage,
            @RequestParam("secondaryImage") MultipartFile secondaryImage,
            @RequestParam(value = "caption", required = false) String caption,
            @RequestParam(value = "visibility", defaultValue = "PUBLIC") String visibility,
            @RequestHeader("Authorization") String authHeader) throws IOException { // ← Получаем токен из заголовка

        Long userId = extractUserIdFromToken(authHeader); // ← Используем новый метод
        log.info("User {} creating new post", userId);

        String primaryUrl = imageService.uploadImage(primaryImage, userId, "primary");
        String secondaryUrl = imageService.uploadImage(secondaryImage, userId, "secondary");

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

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPost(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        Long currentUserId = extractUserIdFromToken(authHeader);

        return postService.getVisiblePostById(id, currentUserId)
                .map(PostMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<PostDTO>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "postedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestHeader("Authorization") String authHeader) {

        Long userId = extractUserIdFromToken(authHeader);

        Sort.Direction direction = sortDir.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<PostDTO> posts = postService.getVisiblePostsPaginated(userId, pageable)
                .map(PostMapper::toDto);

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/friends")
    public ResponseEntity<List<PostDTO>> getFriendsPosts(
            @RequestHeader("Authorization") String authHeader) {
        Long userId = extractUserIdFromToken(authHeader);

        List<PostDTO> posts = postService.getFriendsPosts(userId)
                .stream()
                .map(PostMapper::toDto)
                .toList();

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/today")
    public ResponseEntity<List<PostDTO>> getTodayPosts(
            @RequestHeader("Authorization") String authHeader) {
        Long userId = extractUserIdFromToken(authHeader);

        List<PostDTO> posts = postService.getTodayPosts()
                .stream()
                .filter(post -> post.getUserId().equals(userId) ||
                        post.getVisibility() == Post.Visibility.PUBLIC)
                .map(PostMapper::toDto)
                .toList();

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostDTO>> getUserPosts(
            @PathVariable Long userId,
            @RequestHeader("Authorization") String authHeader) {

        Long currentUserId = extractUserIdFromToken(authHeader);

        List<PostDTO> posts = postService.getUserPosts(userId)
                .stream()
                .filter(post -> {
                    if (post.getUserId().equals(currentUserId)) {
                        return true;
                    }
                    return post.getVisibility() == Post.Visibility.PUBLIC;
                })
                .map(PostMapper::toDto)
                .toList();

        return ResponseEntity.ok(posts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> updatePost(
            @PathVariable Long id,
            @RequestParam(value = "caption", required = false) String caption,
            @RequestParam(value = "visibility", required = false) String visibility,
            @RequestHeader("Authorization") String authHeader) { // ← Добавили authHeader

        Long currentUserId = extractUserIdFromToken(authHeader);

        Post updateData = new Post();
        updateData.setCaption(caption);

        if (visibility != null) {
            updateData.setVisibility(Post.Visibility.valueOf(visibility.toUpperCase()));
        }

        Post updated = postService.updatePost(id, updateData, currentUserId); // ← Передаём userId
        return ResponseEntity.ok(PostMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) { // ← Добавили authHeader

        Long currentUserId = extractUserIdFromToken(authHeader);
        postService.deletePost(id, currentUserId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Извлекает userId из JWT токена
     */
    private Long extractUserIdFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalStateException("Invalid Authorization header");
        }

        String token = authHeader.substring(7);
        return jwtUtil.extractUserId(token); // ← Используем метод из JwtUtil
    }
}