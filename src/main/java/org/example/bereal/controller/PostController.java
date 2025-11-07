package org.example.bereal.controller;
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

    // --- C (Create) ---
    // POST /api/posts
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        // В реальном приложении здесь будет @RequestParam MultipartFile для файлов
        Post createdPost = postService.createPost(post);
        return ResponseEntity.ok(createdPost);
    }

    // --- R (Read - By ID) ---
    // GET /api/posts/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable Long id) {
        return postService.getPostById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // --- R (Read - All) ---
    // GET /api/posts
    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    // --- U (Update) ---
    // PUT /api/posts/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post postDetails) {
        try {
            Post updatedPost = postService.updatePost(id, postDetails);
            return ResponseEntity.ok(updatedPost);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- D (Delete) ---
    // DELETE /api/posts/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
