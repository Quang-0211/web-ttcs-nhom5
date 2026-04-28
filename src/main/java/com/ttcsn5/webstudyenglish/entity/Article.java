package com.ttcsn5.webstudyenglish.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "article")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String image;
    private String audio;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "cate_id")
    private Category category;

    public Article(String title, String content, String image, String audio, Boolean status, Category category) {
        this.title = title;
        this.content = content;
        this.image = image;
        this.audio = audio;
        this.status = status;
        this.category = category;
    }

    public Article(int id, String title, String content, String image, String audio, Boolean status, Category category) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.image = image;
        this.audio = audio;
        this.status = status;
        this.category = category;
    }

    @PrePersist
    public void createAt() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void updateAt() {
        this.updatedAt = LocalDateTime.now();
    }

}