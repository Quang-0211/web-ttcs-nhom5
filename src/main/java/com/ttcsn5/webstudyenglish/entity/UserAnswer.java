package com.ttcsn5.webstudyenglish.entity;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "user_answer")
@Data
@NoArgsConstructor
public class UserAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private UserQuizAttempt attempt;

    @ManyToOne
    private Question question;

    private Integer selectedChoiceId;

    @Column(columnDefinition = "TEXT")
    private String userAnswerText;

    private Boolean isCorrect;
}
