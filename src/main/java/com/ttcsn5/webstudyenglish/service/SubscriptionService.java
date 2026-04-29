package com.ttcsn5.webstudyenglish.service;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ttcsn5.webstudyenglish.entity.Subscription;
import com.ttcsn5.webstudyenglish.repository.SubscriptionRepo;

@Service
public class SubscriptionService {
    @Autowired
    private SubscriptionRepo subscriptionRepo;

    public Set<Subscription> getSubscriptionRepobyUserId(Long userId) {
        return subscriptionRepo.findSetSubscriptionsByUserId(userId);
    }

    public void saveSubscription(Subscription subscription) {
        subscriptionRepo.save(subscription);
    }

    public Page<Subscription> searchSubscription(String planName, LocalDateTime start, LocalDateTime end,
            Pageable pageable) {

        // Nếu không chọn ngày, ta có thể set khoảng thời gian mặc định cực rộng
        if (start == null) {
            start = LocalDateTime.of(1970, 1, 1, 0, 0); // Mặc định từ xa xưa
        }
        if (end == null) {
            end = LocalDateTime.now(); // Mặc định đến hiện tại
        }

        // Thực hiện tìm kiếm
        return subscriptionRepo.findByPlanNameContainingAndActiveAndStartDateBetween(
                planName, true, start, end, pageable);
    }

}
