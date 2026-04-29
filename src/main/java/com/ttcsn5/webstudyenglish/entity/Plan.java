package com.ttcsn5.webstudyenglish.entity;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "plan")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private Double price;
    private Integer durationDays;

    // ================== MANY TO MANY ==================

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "plan_article",
            joinColumns = @JoinColumn(name = "plan_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> articles;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "plan_quiz",
            joinColumns = @JoinColumn(name = "plan_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> quizzes;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "plan_video",
            joinColumns = @JoinColumn(name = "plan_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> videos;

    // ❗ 3 cái này FIX theo DB của bạn

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "plan_dictation",
            joinColumns = @JoinColumn(name = "plan_id"),
            inverseJoinColumns = @JoinColumn(name = "dictation_id"))
    private Set<Category> dictationTopics;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "plan_grammar",
            joinColumns = @JoinColumn(name = "plan_id"),
            inverseJoinColumns = @JoinColumn(name = "grammar_id"))
    private Set<Category> grammar;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "plan_vocabulary",
            joinColumns = @JoinColumn(name = "plan_id"),
            inverseJoinColumns = @JoinColumn(name = "vocabulary_id"))
    private Set<Category> vocabulary;

    // ================== INFO ==================

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    private Boolean active = true;

    // ================== AUTO TIME ==================


    public Plan(String name, Double price, Integer durationDays, Set<Category> articles, Set<Category> quizzes,
                Set<Category> videos, Set<Category> dictationTopics, Set<Category> grammar, Set<Category> vocabulary,
                String description, Boolean active, LocalDateTime created_at) {
        this.name = name;
        this.price = price;
        this.durationDays = durationDays;
        this.articles = articles;
        this.quizzes = quizzes;
        this.videos = videos;
        this.dictationTopics = dictationTopics;
        this.grammar = grammar;
        this.vocabulary = vocabulary;
        this.description = description;
        this.created_at = created_at;
        this.active = active;
    }

    public Plan(Integer id, String name, Double price, Integer durationDays, Set<Category> articles,
                Set<Category> quizzes,
                Set<Category> videos, Set<Category> dictationTopics, Set<Category> grammar, Set<Category> vocabulary,
                String description, Boolean active, LocalDateTime updated_at) {
        this.id=id;
        this.name = name;
        this.price = price;
        this.durationDays = durationDays;
        this.articles = articles;
        this.quizzes = quizzes;
        this.videos = videos;
        this.dictationTopics = dictationTopics;
        this.grammar = grammar;
        this.vocabulary = vocabulary;
        this.description = description;
        this.updated_at = updated_at;
        this.active = active;
    }
}