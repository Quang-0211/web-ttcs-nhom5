package com.ttcsn5.webstudyenglish.controller.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ttcsn5.webstudyenglish.entity.Answer;
import com.ttcsn5.webstudyenglish.entity.Question;
import com.ttcsn5.webstudyenglish.entity.Quiz;
import com.ttcsn5.webstudyenglish.entity.User;
import com.ttcsn5.webstudyenglish.entity.UserAnswer;
import com.ttcsn5.webstudyenglish.entity.UserQuizAttempt;
import com.ttcsn5.webstudyenglish.service.QuizService;
import com.ttcsn5.webstudyenglish.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserQuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private UserService userService;

    @GetMapping("/quizzes")
    public String getQuizzes(Model model, HttpSession session) {
        List<Quiz> quizzes = quizService.findAll();
        model.addAttribute("quizzes", quizzes);
        model.addAttribute("activeMenu", "quiz");
        model.addAttribute("userPath", "user/quiz/list");

        Object roleIdObj = session.getAttribute("roleId");
        if (roleIdObj != null) {
            model.addAttribute("roleId", (int) roleIdObj);
        }

        return "user/index";
    }

    @GetMapping("/quiz/{id}")
    public String takeQuiz(@PathVariable Integer id, Model model, HttpSession session) {
        Optional<Quiz> quizOpt = quizService.findById(id);
        if (!quizOpt.isPresent()) {
            return "redirect:/quizzes";
        }

        Quiz quiz = quizOpt.get();
        List<Question> questions = quizService.findQuestionsByQuiz(quiz);

        // Load answers for each question
        for (Question question : questions) {
            List<Answer> answers = quizService.findAnswersByQuestion(question);
            question.setAnswers(answers);
        }

        model.addAttribute("quiz", quiz);
        model.addAttribute("questions", questions);
        model.addAttribute("activeMenu", "quiz");
        model.addAttribute("userPath", "user/quiz/take");

        Object roleIdObj = session.getAttribute("roleId");
        if (roleIdObj != null) {
            model.addAttribute("roleId", (int) roleIdObj);
        }

        return "user/index";
    }

    @PostMapping("/quiz/{quizId}/submit")
    @ResponseBody
    public String submitQuiz(@PathVariable Integer quizId, @RequestBody List<UserAnswer> userAnswers, HttpSession session) {
        Optional<Quiz> quizOpt = quizService.findById(quizId);
        if (!quizOpt.isPresent()) {
            return "error";
        }

        Quiz quiz = quizOpt.get();
        User user = getCurrentUser(session);
        if (user == null) {
            return "not_logged_in";
        }

        // Create attempt
        UserQuizAttempt attempt = new UserQuizAttempt();
        attempt.setUser(user);
        attempt.setQuiz(quiz);
        attempt.setStartedAt(LocalDateTime.now());
        attempt.setStatus("completed");
        attempt.setCompletedAt(LocalDateTime.now());

        // Calculate score
        int correctCount = 0;
        int totalQuestions = userAnswers.size();

        for (UserAnswer userAnswer : userAnswers) {
            userAnswer.setAttempt(attempt);

            // Find the question and check correctness
            Optional<Question> questionOpt = quizService.findById(userAnswer.getQuestion().getId())
                .flatMap(q -> quizService.findQuestionsByQuiz(q).stream()
                    .filter(qst -> qst.getId().equals(userAnswer.getQuestion().getId()))
                    .findFirst());

            if (questionOpt.isPresent()) {
                Question question = questionOpt.get();
                userAnswer.setQuestion(question);

                // Check if answer is correct
                List<Answer> answers = quizService.findAnswersByQuestion(question);
                boolean isCorrect = answers.stream()
                    .anyMatch(ans -> ans.getId().equals(userAnswer.getSelectedChoiceId()) && ans.getIsCorrect());

                userAnswer.setIsCorrect(isCorrect);
                if (isCorrect) {
                    correctCount++;
                }

                // Save user answer
                quizService.saveUserAnswer(userAnswer);
            }
        }

        float score = totalQuestions > 0 ? (float) correctCount / totalQuestions * 100 : 0;
        attempt.setScore(score);

        // Save attempt
        quizService.saveAttempt(attempt);

        return "success";
    }

    private User getCurrentUser(HttpSession session) {
        Object userIdObj = session.getAttribute("userId");
        if (userIdObj != null) {
            Integer userId = (Integer) userIdObj;
            return userService.findById(userId).orElse(null);
        }
        return null;
    }
}