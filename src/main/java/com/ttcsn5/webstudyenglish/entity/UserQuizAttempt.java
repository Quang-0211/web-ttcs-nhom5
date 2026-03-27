package com.ttcsn5.webstudyenglish.entity;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "user_quiz_attempt")
@Data
@NoArgsConstructor
public class UserQuizAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Quiz quiz;

    private Float score;
    private String status;

    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
}
