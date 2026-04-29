package com.ttcsn5.webstudyenglish.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ttcsn5.webstudyenglish.entity.User;
import com.ttcsn5.webstudyenglish.entity.UserVocabulary;
import com.ttcsn5.webstudyenglish.entity.Vocabulary;

@Repository
public interface UserVocabularyRepo extends JpaRepository<UserVocabulary, Integer> {
    
    Optional<UserVocabulary> findByUserAndVocabulary(User user, Vocabulary vocabulary);
    
    List<UserVocabulary> findByUserOrderByIdDesc(User user);
    
    boolean existsByUserAndVocabulary(User user, Vocabulary vocabulary);
    
    void deleteByUserAndVocabulary(User user, Vocabulary vocabulary);
}
