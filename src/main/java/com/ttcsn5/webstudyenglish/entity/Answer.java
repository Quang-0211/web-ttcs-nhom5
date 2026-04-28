package com.ttcsn5.webstudyenglish.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "answer")
@Data
@NoArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Boolean isCorrect = false;

    private Integer orderIndex = 0;

    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    @org.hibernate.annotations.UpdateTimestamp
    private LocalDateTime updated_at;
}