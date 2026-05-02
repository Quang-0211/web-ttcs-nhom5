package com.ttcsn5.webstudyenglish.controller.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ttcsn5.webstudyenglish.entity.Answer;
import com.ttcsn5.webstudyenglish.entity.Category;
import com.ttcsn5.webstudyenglish.entity.Question;
import com.ttcsn5.webstudyenglish.entity.Quiz;
import com.ttcsn5.webstudyenglish.entity.Subscription;
import com.ttcsn5.webstudyenglish.entity.User;
import com.ttcsn5.webstudyenglish.entity.UserAnswer;
import com.ttcsn5.webstudyenglish.entity.UserQuizAttempt;
import com.ttcsn5.webstudyenglish.repository.CategoryRepo;
import com.ttcsn5.webstudyenglish.repository.QuestionRepo;
import com.ttcsn5.webstudyenglish.repository.QuizRepo;
import com.ttcsn5.webstudyenglish.service.QuizService;
import com.ttcsn5.webstudyenglish.service.SubscriptionService;
import com.ttcsn5.webstudyenglish.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserQuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private QuestionRepo questionRepo;

    @Autowired
    private QuizRepo quizRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping("/quizzes")
    public String getQuizzes(
            @RequestParam(name = "cnt", required = false, defaultValue = "0") Integer cnt,
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "categorySearch", required = false, defaultValue = "0") Integer categorySearch,
            Model model, HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRoleId().getCode().equals("USER")) {
            return "redirect:/login";
        }

        Set<Subscription> subscriptions = subscriptionService.getSubscriptionRepobyUserId(user.getId());

        Set<Category> categories = subscriptions.stream().map(subscription -> subscription.getPlan().getQuizzes())
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
        Pageable pageable = PageRequest.of(cnt, 12, Sort.by("created_at").descending());

        model.addAttribute("quizzes",
                quizRepo.findQuizUserHomeAndCategoryPlan(pageable, keyword, categorySearch, categories));
        model.addAttribute("activeMenu", "quiz");
        model.addAttribute("userPath", "user/quiz/list");
        model.addAttribute("categories", categories);

        model.addAttribute("keyword", keyword);
        model.addAttribute("categorySearch", categorySearch);
        model.addAttribute("cnt", cnt);

        // Object roleIdObj = session.getAttribute("roleId");
        // if (roleIdObj != null) {
        // model.addAttribute("roleId", (int) roleIdObj);
        // }

        return "user/index";
    }

    @GetMapping("/user/quiz/take/{id}")
    public String takeQuiz(@PathVariable Integer id, Model model, HttpSession session) {

        Quiz quiz = quizService.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        List<Question> questions = questionRepo.findByQuizWithAnswers(quiz);

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
    public String submitQuiz(@PathVariable Integer quizId,
            @RequestBody List<UserAnswer> userAnswers,
            HttpSession session) {

        Quiz quiz = quizService.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "not_logged_in";
        }

        // Load full question + answers (1 query)
        List<Question> questions = questionRepo.findByQuizWithAnswers(quiz);

        // Map để lookup nhanh
        Map<Integer, Question> questionMap = questions.stream()
                .collect(Collectors.toMap(Question::getId, q -> q));

        // Create attempt
        UserQuizAttempt attempt = new UserQuizAttempt();
        attempt.setUser(user);
        attempt.setQuiz(quiz);
        attempt.setStartedAt(LocalDateTime.now());
        attempt.setCompletedAt(LocalDateTime.now());
        attempt.setStatus("completed");

        int correctCount = 0;

        for (UserAnswer ua : userAnswers) {

            Question question = questionMap.get(ua.getQuestion().getId());
            if (question == null)
                continue;

            ua.setAttempt(attempt);
            ua.setQuestion(question);

            // check đúng
            boolean isCorrect = question.getAnswers().stream()
                    .anyMatch(a -> a.getId().equals(ua.getSelectedChoiceId())
                            && Boolean.TRUE.equals(a.getIsCorrect()));

            ua.setIsCorrect(isCorrect);

            if (isCorrect)
                correctCount++;

            quizService.saveUserAnswer(ua);
        }

        int totalQuestions = questions.size();

        float score = totalQuestions > 0
                ? (float) correctCount / totalQuestions * 100
                : 0;

        attempt.setScore(score);

        quizService.saveAttempt(attempt);

        return String.valueOf(correctCount);
    }

    @GetMapping("/quiz/create")
    public String createQuiz(Model model, HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }

        model.addAttribute("categories", categoryRepo.findAll());
        model.addAttribute("activeMenu", "quiz");
        model.addAttribute("userPath", "user/quiz/create");

        Object roleIdObj = session.getAttribute("roleId");
        if (roleIdObj != null) {
            model.addAttribute("roleId", (int) roleIdObj);
        }

        return "user/index";
    }

    @GetMapping("/quiz/edit/{id}")
    public String editQuiz(@PathVariable Integer id, Model model, HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }

        Optional<Quiz> quizOpt = quizService.findById(id);
        if (!quizOpt.isPresent()) {
            return "redirect:/quizzes";
        }

        model.addAttribute("categories", categoryRepo.findAll());
        model.addAttribute("activeMenu", "quiz");
        model.addAttribute("userPath", "user/quiz/edit");

        Object roleIdObj = session.getAttribute("roleId");
        if (roleIdObj != null) {
            model.addAttribute("roleId", (int) roleIdObj);
        }

        return "user/index";
    }
}
