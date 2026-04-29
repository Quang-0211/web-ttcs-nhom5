package com.ttcsn5.webstudyenglish.repository;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ttcsn5.webstudyenglish.entity.Subscription;

@Repository
public interface SubscriptionRepo extends JpaRepository<Subscription, Integer> {
    @Query("select s from Subscription s where s.user.id = :userId and s.endDate > CURRENT_TIMESTAMP and s.active = true")
    Set<Subscription> findSetSubscriptionsByUserId(@Param("userId") Long userId);

    Page<Subscription> findByPlanNameContainingAndActiveAndStartDateBetween(
            String planName,
            Boolean active,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable);
}
