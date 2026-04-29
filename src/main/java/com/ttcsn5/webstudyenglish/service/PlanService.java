package com.ttcsn5.webstudyenglish.service;

import java.util.List;
import java.util.Set;

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

    public List<Plan> searchPlansUser(String name, Boolean active, Double maxPrice) {
        Pageable pageable = PageRequest.of(0, 100, Sort.by("price").ascending());

        return planRepository.searchPlans(
                (name == null || name.isBlank()) ? null : name.trim(),
                active,
                (maxPrice == null || maxPrice <= 0) ? null : maxPrice,
                pageable).getContent();
    }

    public Plan getPlanById(Integer id) {
        return planRepository.findById(id).orElse(null);
    }

    public Plan getByName(String name) {
        return planRepository.findByName(name);
    }

}
