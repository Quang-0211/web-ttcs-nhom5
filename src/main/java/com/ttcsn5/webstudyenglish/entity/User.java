package com.ttcsn5.webstudyenglish.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table
@NoArgsConstructor
@Data


public class User {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length=50)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique=true, length=50)
    private String email;
    private int point;
    @Column(nullable = false, name="streak_cnt")
    private int streakCnt;
    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    @ManyToOne
    @JoinColumn(name = "relo_id")
    private Role roleId;
}
