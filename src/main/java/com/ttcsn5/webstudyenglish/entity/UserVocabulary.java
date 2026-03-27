package com.ttcsn5.webstudyenglish.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_vocabulary")
@Data
@NoArgsConstructor
public class UserVocabulary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private User user;

    @ManyToOne
    @JoinColumn(name="vocab_id")
    private Vocabulary vocabulary;

    private Integer level = 0;
    private Float easeFactor = 2.5f;
    private Integer repetitions = 0;
    private Integer reviewInterval = 0;

    private LocalDateTime lastReviewedAt;
    private LocalDateTime nextReviewDate;
}
