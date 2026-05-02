package com.ttcsn5.webstudyenglish.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ttcsn5.webstudyenglish.entity.UserQuizAttempt;

@Repository
public interface UserQuizAttemptRepo extends JpaRepository<UserQuizAttempt, Integer> {
    @Query("SELECT AVG(u.score) FROM UserQuizAttempt u WHERE u.user.id = :userId")
    Double getAverageScoreByUserId(@Param("userId") Long userId);

    // Nếu bạn muốn đếm xem user đã làm bao nhiêu bài
    Long countByUserId(@Param("userId") Long userId);
}