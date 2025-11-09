package org.example.bereal.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    private String primaryImageUrl;
    private String secondaryImageUrl;

    @Column(nullable = false, updatable = false)
    private LocalDateTime postedAt;

    private boolean isLate = false;
    private String caption;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Visibility visibility = Visibility.PUBLIC;

    @PrePersist
    protected void onCreate() {
        this.postedAt = LocalDateTime.now();
    }

    public enum Visibility {
        PUBLIC, FRIENDS_ONLY, PRIVATE
    }
}


