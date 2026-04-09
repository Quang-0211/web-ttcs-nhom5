package com.ttcsn5.webstudyenglish.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ttcsn5.webstudyenglish.entity.Grammar;

@Repository
public interface GrammarRepo extends JpaRepository<Grammar, Integer> {
}
