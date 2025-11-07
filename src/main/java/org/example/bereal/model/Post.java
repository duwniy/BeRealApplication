package org.example.bereal.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Data // Аннотация Lombok для автоматической генерации геттеров, сеттеров и toString
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID пользователя, который сделал пост
    private Long userId;

    // URL для основного (заднего) изображения
    private String primaryImageUrl;

    // URL для вторичного (селфи) изображения
    private String secondaryImageUrl;

    // Время публикации
    private LocalDateTime postedAt;

    // Флаг, указывающий, был ли пост сделан поздно (за пределами 2-мин. окна)
    private boolean isLate = false;

    // Конструкторы, геттеры/сеттеры (генерируются Lombok @Data)
}

