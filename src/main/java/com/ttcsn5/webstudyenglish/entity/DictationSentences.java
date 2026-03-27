package com.ttcsn5.webstudyenglish.entity;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "dictation_sentences")
@Data
@NoArgsConstructor
public class DictationSentences {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private DictationTopics topic;

    private String title;
    private String audioUrl;
    private Integer correctIndex;
}
