package com.ttcsn5.webstudyenglish.entity;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "dictation_topics")
@Data
@NoArgsConstructor
public class DictationTopics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    private Category category;
    @JoinColumn(name="cate_id")
    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}