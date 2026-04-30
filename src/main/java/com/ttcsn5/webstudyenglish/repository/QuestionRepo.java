package com.ttcsn5.webstudyenglish.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ttcsn5.webstudyenglish.entity.Question;
import com.ttcsn5.webstudyenglish.entity.Quiz;

@Repository
public interface QuestionRepo extends JpaRepository<Question, Integer> {
    List<Question> findByQuizOrderByQuestionOrder(Quiz quiz);
}