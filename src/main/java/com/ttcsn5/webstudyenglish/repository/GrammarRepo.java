package com.ttcsn5.webstudyenglish.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ttcsn5.webstudyenglish.entity.Grammar;

@Repository
public interface GrammarRepo extends JpaRepository<Grammar, Integer> {
    List<Grammar> findByCategoryId(Integer categoryId);
    List<Grammar> findByCategoryIdAndNameContainingIgnoreCase(Integer categoryId, String name);
}
