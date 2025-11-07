package org.example.bereal.service;

import org.example.bereal.model.Post;
import org.example.bereal.repository.PostRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // --- CREATE (Создание поста) ---
    public Post createPost(Post postData) {
        LocalDateTime now = LocalDateTime.now();
        // В реальном приложении здесь будет логика проверки 2-минутного окна.
        // Сейчас просто установим время публикации.
        postData.setPostedAt(now);
        // postData.setIsLate(checkIfLate(now)); // Предполагаемая логика

        // Здесь также будет логика загрузки изображений в S3/MinIO
        // postData.setPrimaryImageUrl(uploadFile(postData.getPrimaryFile()));

        return postRepository.save(postData);
    }

    // --- READ (Получение по ID) ---
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    // --- READ (Получение всех постов - для примера) ---
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // --- UPDATE (Обновление поста - редко используется в стиле BeReal, но возможно для подписи) ---
    public Post updatePost(Long id, Post postDetails) {
        // Предположим, можно обновить только подпись или статус
        // post.setCaption(postDetails.getCaption());
        // post.setIsLate(postDetails.isLate());
        return postRepository.findById(id).map(postRepository::save).orElseThrow(() -> new RuntimeException("Post not found with id " + id));
    }

    // --- DELETE (Удаление поста) ---
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}