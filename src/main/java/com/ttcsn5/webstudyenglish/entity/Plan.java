package com.ttcsn5.webstudyenglish.entity;
import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "plan")
@Data
@NoArgsConstructor
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private Double price;
    private Integer durationDays;
    @ManyToMany
    private Set<Article> articles;
    @ManyToMany
    private Set<Quiz> quizzes;
    @ManyToMany
    private Set<Video> videos;
    @ManyToMany
    private Set<DictationTopics> dictationTopics;
    @ManyToMany
    private Set<Grammar> grammar;
    @ManyToMany
    private Set<Vocabulary> vocabulary;

    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    private Boolean active = true;
}
