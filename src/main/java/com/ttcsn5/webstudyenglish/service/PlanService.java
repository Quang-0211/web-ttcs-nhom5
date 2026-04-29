package com.ttcsn5.webstudyenglish.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ttcsn5.webstudyenglish.entity.Plan;
import com.ttcsn5.webstudyenglish.repository.PlanRepository;

@Service
public class PlanService {

    @Autowired
    private PlanRepository planRepository;

    public List<Plan> getAllPlans() {
        return planRepository.findAll();
    }

    public List<Plan> searchPlans(String name, Boolean active, Double maxPrice, int cnt) {
        Pageable pageable = PageRequest.of(cnt, 10, Sort.by("id").ascending());

        return planRepository.searchPlans(
                (name == null || name.isBlank()) ? null : name.trim(),
                active,
                (maxPrice == null || maxPrice <= 0) ? null : maxPrice,
                pageable).getContent();
    }

    public void createPlan(Plan plan) {
        planRepository.save(plan);
    }
}
