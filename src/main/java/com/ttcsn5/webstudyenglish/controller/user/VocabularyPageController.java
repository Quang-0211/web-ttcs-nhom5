package com.ttcsn5.webstudyenglish.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.ttcsn5.webstudyenglish.entity.Category;
import com.ttcsn5.webstudyenglish.entity.Vocabulary;
import com.ttcsn5.webstudyenglish.service.CategoryService;
import com.ttcsn5.webstudyenglish.service.VocabularyService;
import com.ttcsn5.webstudyenglish.entity.Subscription;
import com.ttcsn5.webstudyenglish.entity.User;
import com.ttcsn5.webstudyenglish.service.SubscriptionService;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpSession;

@Controller
public class VocabularyPageController {

    @Autowired
    private VocabularyService vocabularyService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping("/vocabularies")
    public String getTopics(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        Set<Subscription> subscriptions = subscriptionService.getSubscriptionRepobyUserId(user.getId());
        Set<Category> topics = subscriptions.stream().map(subscription -> subscription.getPlan().getVocabulary())
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
        model.addAttribute("topics", topics);
        model.addAttribute("activeMenu", "vocabulary");
        model.addAttribute("userPath", "user/vocabulary/topics");
        Object roleIdObj = session.getAttribute("roleId");
        if (roleIdObj != null) {
            model.addAttribute("roleId", (int) roleIdObj);
        }
        return "user/index";
    }

    @GetMapping("/vocabularies/category/{id}")
    public String getVocabulariesByCategory(
            @PathVariable("id") Integer categoryId,
            @RequestParam(value = "search", required = false) String search,
            Model model,
            HttpSession session) {
        
        Category category = categoryService.findById(categoryId);
        List<Vocabulary> vocabularies = vocabularyService.searchVocabularies(categoryId, search);
        
        model.addAttribute("category", category);
        model.addAttribute("vocabularies", vocabularies);
        model.addAttribute("search", search);
        model.addAttribute("activeMenu", "vocabulary");
        model.addAttribute("userPath", "user/vocabulary/list");
        
        Object roleIdObj = session.getAttribute("roleId");
        if (roleIdObj != null) {
            model.addAttribute("roleId", (int) roleIdObj);
        }
        
        return "user/index";
    }

    @GetMapping("/vocabularies/{id}")
    public String getVocabularyDetail(@PathVariable("id") Integer id, Model model, HttpSession session) {
        Vocabulary vocabulary = vocabularyService.getVocabularyById(id);
        model.addAttribute("vocab", vocabulary);
        model.addAttribute("activeMenu", "vocabulary");
        model.addAttribute("userPath", "user/vocabulary/detail");
        
        Object roleIdObj = session.getAttribute("roleId");
        if (roleIdObj != null) {
            model.addAttribute("roleId", (int) roleIdObj);
        }
        
        return "user/index";
    }

    @GetMapping("/review/flashcards")
    public String getFlashcards(
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            Model model,
            HttpSession session) {
        Category category = null;
        if (categoryId != null) {
            category = categoryService.findById(categoryId);
            model.addAttribute("category", category);
        }

        List<Vocabulary> flashcards = categoryId != null
                ? vocabularyService.getFlashcards(categoryId)
                : java.util.Collections.emptyList();

        model.addAttribute("flashcards", flashcards);
        model.addAttribute("pageTitle", category != null ? category.getName() + " Flashcards" : "Vocabulary Flashcards");
        
        Object roleIdObj = session.getAttribute("roleId");
        if (roleIdObj != null) {
            model.addAttribute("roleId", (int) roleIdObj);
        }
        
        model.addAttribute("activeMenu", "vocabulary");
        model.addAttribute("userPath", "user/vocabulary/flashcard");
        return "user/index";
    }

    @GetMapping("/review/quiz")
    public String getVocabularyQuiz(
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            Model model,
            HttpSession session) {

        Category category = null;
        if (categoryId != null) {
            category = categoryService.findById(categoryId);
            model.addAttribute("category", category);
        }

        List<Vocabulary> quizCards = categoryId != null
                ? vocabularyService.getFlashcards(categoryId)
                : java.util.Collections.emptyList();

        model.addAttribute("flashcards", quizCards);
        model.addAttribute("pageTitle", category != null ? category.getName() + " Quiz" : "Vocabulary Quiz");

        Object roleIdObj = session.getAttribute("roleId");
        if (roleIdObj != null) {
            model.addAttribute("roleId", (int) roleIdObj);
        }

        model.addAttribute("activeMenu", "vocabulary");
        model.addAttribute("userPath", "user/vocabulary/quiz");
        return "user/index";
    }
}
