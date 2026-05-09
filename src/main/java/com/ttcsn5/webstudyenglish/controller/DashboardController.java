package com.ttcsn5.webstudyenglish.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.ttcsn5.webstudyenglish.entity.User;
import com.ttcsn5.webstudyenglish.repository.*;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private ArticleRepo articleRepo;
    @Autowired
    private QuizRepo quizRepo;
    @Autowired
    private VideoRepo videoRepo;
    @Autowired
    private VocabularyRepo vocabularyRepo;
    @Autowired
    private GrammarRepo grammarRepo;
    @Autowired
    private DictationTopicRepo dictationTopicRepo;
    @Autowired
    private SubscriptionRepo subscriptionRepo;
    @Autowired
    private PlanRepository planRepo;

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRoleId().getCode().equals("ADMIN")) {
            return "redirect:/login";
        }
        String username = user.getUsername();

        model.addAttribute("totalUser", accountRepo.countUser());
        model.addAttribute("totalArticle", articleRepo.countArticle());
        model.addAttribute("totalQuiz", quizRepo.countQuiz());
        model.addAttribute("totalVideo", videoRepo.countVideo());
        model.addAttribute("totalVocabulary", vocabularyRepo.countVocabulary());
        model.addAttribute("totalGrammar", grammarRepo.countGrammar());
        model.addAttribute("totalDictationTopics", dictationTopicRepo.countDictationTopics());
        model.addAttribute("totalRevenue", subscriptionRepo.sumRevenue());
        model.addAttribute("totalSubscription", subscriptionRepo.countSubscription());
        model.addAttribute("totalPlan", planRepo.countPlan());
        model.addAttribute("activePage", "dashboard");
        model.addAttribute("username", username.toUpperCase());
        model.addAttribute("path", "admin/dashboard");
        return "admin/adminhome";
    }

}
