package com.ttcsn5.webstudyenglish.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ttcsn5.webstudyenglish.entity.Answer;
import com.ttcsn5.webstudyenglish.entity.Question;

@Repository
public interface AnswerRepo extends JpaRepository<Answer, Integer> {
    List<Answer> findByQuestionOrderByOrderIndex(Question question);
}