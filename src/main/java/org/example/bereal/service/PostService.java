package org.example.bereal.service;

import org.example.bereal.model.Post;
import org.example.bereal.repository.PostRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createPost(Post post) {
        if (post.getUserId() == null)
            throw new IllegalArgumentException("User ID cannot be null");

        post.setLate(checkIfLate(LocalDateTime.now()));
        return postRepository.save(post);
    }


    private boolean checkIfLate(LocalDateTime postedAt) {
        LocalDateTime callTime = postedAt.toLocalDate().atTime(9, 0); // 9:00 утра
        return postedAt.isAfter(callTime.plusMinutes(2));
    }


    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post updatePost(Long id, Post newPost) {
        return postRepository.findById(id)
                .map(existing -> {
                    existing.setPrimaryImageUrl(newPost.getPrimaryImageUrl());
                    existing.setSecondaryImageUrl(newPost.getSecondaryImageUrl());
                    existing.setCaption(newPost.getCaption());
                    existing.setVisibility(newPost.getVisibility());
                    return postRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public List<Post> getTodayPosts() {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(LocalTime.MAX);
        return postRepository.findByPostedAtBetween(start, end);
    }
}
