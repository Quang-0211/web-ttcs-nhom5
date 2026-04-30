package com.ttcsn5.webstudyenglish.controller.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ttcsn5.webstudyenglish.entity.Answer;
import com.ttcsn5.webstudyenglish.entity.Category;
import com.ttcsn5.webstudyenglish.entity.Question;
import com.ttcsn5.webstudyenglish.entity.Quiz;
import com.ttcsn5.webstudyenglish.service.CategoryService;
import com.ttcsn5.webstudyenglish.service.QuizService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/quiz")
public class UserQuizApiController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Quiz>> getAllQuizzes() {
        List<Quiz> quizzes = quizService.findAll();
        return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuiz(@PathVariable Integer id) {
        Optional<Quiz> quiz = quizService.findById(id);
        return quiz.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Quiz> createQuiz(@RequestBody Quiz quiz, HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return ResponseEntity.status(401).build();
        }
        
        if (quiz.getCategory() != null && quiz.getCategory().getId() != null) {
            Category category = categoryService.findById(quiz.getCategory().getId());
            if (category != null) {
                quiz.setCategory(category);
            }
        }
        Quiz savedQuiz = quizService.save(quiz);
        return ResponseEntity.ok(savedQuiz);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Quiz> updateQuiz(@PathVariable Integer id, @RequestBody Quiz quiz, HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return ResponseEntity.status(401).build();
        }
        
        Optional<Quiz> existingQuiz = quizService.findById(id);
        if (!existingQuiz.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        quiz.setId(id);
        if (quiz.getCategory() != null && quiz.getCategory().getId() != null) {
            Category category = categoryService.findById(quiz.getCategory().getId());
            if (category != null) {
                quiz.setCategory(category);
            }
        }
        Quiz savedQuiz = quizService.save(quiz);
        return ResponseEntity.ok(savedQuiz);
    }

    @GetMapping("/{quizId}/questions")
    public ResponseEntity<List<Question>> getQuestions(@PathVariable Integer quizId) {
        Optional<Quiz> quiz = quizService.findById(quizId);
        if (quiz.isPresent()) {
            List<Question> questions = quizService.findQuestionsByQuiz(quiz.get());
            return ResponseEntity.ok(questions);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/questions")
    public ResponseEntity<Question> createQuestion(@RequestBody Question question, HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return ResponseEntity.status(401).build();
        }
        
        if (question.getQuiz() != null && question.getQuiz().getId() != null) {
            Optional<Quiz> quiz = quizService.findById(question.getQuiz().getId());
            if (quiz.isPresent()) {
                question.setQuiz(quiz.get());
            }
        }
        Question savedQuestion = quizService.saveQuestion(question);
        return ResponseEntity.ok(savedQuestion);
    }

    @PutMapping("/questions/{id}")
    public ResponseEntity<Question> updateQuestion(@PathVariable Integer id, @RequestBody Question question, HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return ResponseEntity.status(401).build();
        }
        
        Optional<Question> existingQuestion = quizService.findQuestionById(id);
        if (!existingQuestion.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        question.setId(id);
        if (question.getQuiz() != null && question.getQuiz().getId() != null) {
            Optional<Quiz> quiz = quizService.findById(question.getQuiz().getId());
            if (quiz.isPresent()) {
                question.setQuiz(quiz.get());
            }
        }
        Question savedQuestion = quizService.saveQuestion(question);
        return ResponseEntity.ok(savedQuestion);
    }

    @GetMapping("/questions/{questionId}/answers")
    public ResponseEntity<List<Answer>> getAnswers(@PathVariable Integer questionId) {
        Optional<Question> question = quizService.findQuestionById(questionId);
        if (question.isPresent()) {
            List<Answer> answers = quizService.findAnswersByQuestion(question.get());
            return ResponseEntity.ok(answers);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/answers")
    public ResponseEntity<Answer> createAnswer(@RequestBody Answer answer, HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return ResponseEntity.status(401).build();
        }
        
        if (answer.getQuestion() != null && answer.getQuestion().getId() != null) {
            Optional<Question> question = quizService.findQuestionById(answer.getQuestion().getId());
            if (question.isPresent()) {
                answer.setQuestion(question.get());
            }
        }
        Answer savedAnswer = quizService.saveAnswer(answer);
        return ResponseEntity.ok(savedAnswer);
    }

    @PutMapping("/answers/{id}")
    public ResponseEntity<Answer> updateAnswer(@PathVariable Integer id, @RequestBody Answer answer, HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return ResponseEntity.status(401).build();
        }
        
        answer.setId(id);
        if (answer.getQuestion() != null && answer.getQuestion().getId() != null) {
            Optional<Question> question = quizService.findQuestionById(answer.getQuestion().getId());
            if (question.isPresent()) {
                answer.setQuestion(question.get());
            }
        }
        Answer savedAnswer = quizService.saveAnswer(answer);
        return ResponseEntity.ok(savedAnswer);
    }
}
