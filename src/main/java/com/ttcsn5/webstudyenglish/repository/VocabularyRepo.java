package com.ttcsn5.webstudyenglish.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ttcsn5.webstudyenglish.entity.Vocabulary;

@Repository
public interface VocabularyRepo extends JpaRepository<Vocabulary, Integer> {
    List<Vocabulary> findByCategoryId(Integer categoryId);
    List<Vocabulary> findByCategoryIdAndWordContainingIgnoreCase(Integer categoryId, String word);
}
