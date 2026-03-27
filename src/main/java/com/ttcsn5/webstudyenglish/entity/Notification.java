package com.ttcsn5.webstudyenglish.entity;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;

    @ManyToOne
    @JoinColumn(name = "actor_id")
    private User actor;

    private String type;

    @Column(columnDefinition = "TEXT")
    private String message;

    private String link;

    private Boolean isRead = false;
    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;
}
