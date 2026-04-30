package com.ttcsn5.webstudyenglish.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ttcsn5.webstudyenglish.entity.Category;
import com.ttcsn5.webstudyenglish.entity.Quiz;

@Repository
public interface QuizRepo extends JpaRepository<Quiz, Integer> {
    List<Quiz> findByCategory(Category category);
}