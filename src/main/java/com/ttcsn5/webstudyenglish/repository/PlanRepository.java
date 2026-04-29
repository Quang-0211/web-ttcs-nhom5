package com.ttcsn5.webstudyenglish.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ttcsn5.webstudyenglish.entity.Plan;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Integer> {



    @Query("""
                SELECT p FROM Plan p
                WHERE (:name IS NULL OR p.name LIKE CONCAT('%', :name, '%'))
                AND (:active IS NULL OR p.active = :active)
                AND (:maxPrice IS NULL OR p.price <= :maxPrice)
            """)
    Page<Plan> searchPlans(String name, Boolean active, Double maxPrice, Pageable pageable);
}
