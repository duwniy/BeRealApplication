package org.example.bereal;

import org.example.bereal.model.Post;
import org.example.bereal.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Test
    void createPost_setsPostedAtAutomatically() {
        Post post = new Post();
        post.setUserId(1L);
        Post saved = postService.createPost(post);
        assertNotNull(saved.getPostedAt());
    }
}
