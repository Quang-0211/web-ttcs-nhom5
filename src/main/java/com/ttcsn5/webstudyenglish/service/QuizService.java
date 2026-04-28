package com.ttcsn5.webstudyenglish.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ttcsn5.webstudyenglish.entity.Answer;
import com.ttcsn5.webstudyenglish.entity.Category;
import com.ttcsn5.webstudyenglish.entity.Question;
import com.ttcsn5.webstudyenglish.entity.Quiz;
import com.ttcsn5.webstudyenglish.entity.UserAnswer;
import com.ttcsn5.webstudyenglish.entity.UserQuizAttempt;
import com.ttcsn5.webstudyenglish.repository.AnswerRepo;
import com.ttcsn5.webstudyenglish.repository.CategoryRepo;
import com.ttcsn5.webstudyenglish.repository.QuestionRepo;
import com.ttcsn5.webstudyenglish.repository.QuizRepo;
import com.ttcsn5.webstudyenglish.repository.UserAnswerRepo;
import com.ttcsn5.webstudyenglish.repository.UserQuizAttemptRepo;

@Service
public class QuizService {

    @Autowired
    private QuizRepo quizRepo;

    @Autowired
    private AnswerRepo answerRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private UserQuizAttemptRepo userQuizAttemptRepo;

    @Autowired
    private UserAnswerRepo userAnswerRepo;

    @Autowired
    private QuestionRepo questionRepo;

    public List<Quiz> findAll() {
        return quizRepo.findAll();
    }

    public Optional<Quiz> findById(Integer id) {
        return quizRepo.findById(id);
    }

    public List<Quiz> findByCategory(Category category) {
        return quizRepo.findByCategory(category);
    }

    @Transactional
    public Quiz save(Quiz quiz) {
        if (quiz.getCreated_at() == null) {
            quiz.setCreated_at(LocalDateTime.now());
        }
        quiz.setUpdated_at(LocalDateTime.now());
        return quizRepo.save(quiz);
    }

    @Transactional
    public void deleteById(Integer id) {
        quizRepo.deleteById(id);
    }

    @Transactional
    public Question saveQuestion(Question question) {
        if (question.getCreated_at() == null) {
            question.setCreated_at(LocalDateTime.now());
        }
        question.setUpdated_at(LocalDateTime.now());
        return questionRepo.save(question);
    }

    @Transactional
    public void deleteQuestionById(Integer id) {
        questionRepo.deleteById(id);
    }

    @Transactional
    public void deleteAnswerById(Integer id) {
        answerRepo.deleteById(id);
    }

    public List<Question> findQuestionsByQuiz(Quiz quiz) {
        return questionRepo.findByQuizOrderByQuestionOrder(quiz);
    }

    public Optional<Question> findQuestionById(Integer id) {
        return questionRepo.findById(id);
    }

    @Transactional
    public Answer saveAnswer(Answer answer) {
        if (answer.getCreated_at() == null) {
            answer.setCreated_at(LocalDateTime.now());
        }
        answer.setUpdated_at(LocalDateTime.now());
        return answerRepo.save(answer);
    }

    public List<Answer> findAnswersByQuestion(Question question) {
        return answerRepo.findByQuestionOrderByOrderIndex(question);
    }

    @Transactional
    public UserQuizAttempt saveAttempt(UserQuizAttempt attempt) {
        return userQuizAttemptRepo.save(attempt);
    }

    @Transactional
    public UserAnswer saveUserAnswer(UserAnswer userAnswer) {
        return userAnswerRepo.save(userAnswer);
    }
}