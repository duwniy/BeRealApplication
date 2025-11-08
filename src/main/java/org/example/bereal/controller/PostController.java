package org.example.bereal.controller;

import jakarta.validation.Valid;
import org.example.bereal.dto.PostDTO;
import org.example.bereal.mapper.PostMapper;
import org.example.bereal.model.Post;
import org.example.bereal.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostDTO> createPost(@Valid @RequestBody PostDTO dto) {
        Post created = postService.createPost(PostMapper.fromDto(dto));
        return ResponseEntity.ok(PostMapper.toDto(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Long id) {
        return postService.getPostById(id)
                .map(PostMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<PostDTO> getAllPosts() {
        return postService.getAllPosts().stream().map(PostMapper::toDto).toList();
    }

    @GetMapping("/today")
    public List<PostDTO> getTodayPosts() {
        return postService.getTodayPosts().stream().map(PostMapper::toDto).toList();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> updatePost(@PathVariable Long id, @RequestBody PostDTO dto) {
        Post updated = postService.updatePost(id, PostMapper.fromDto(dto));
        return ResponseEntity.ok(PostMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
