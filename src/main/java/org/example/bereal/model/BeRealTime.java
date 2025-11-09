package org.example.bereal.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "bereal_times")
@Data
public class BeRealTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalDateTime notificationTime;

    @Column(nullable = false)
    private boolean sent = false;

    public BeRealTime() {}

    public BeRealTime(LocalDateTime notificationTime) {
        this.notificationTime = notificationTime;
    }
}