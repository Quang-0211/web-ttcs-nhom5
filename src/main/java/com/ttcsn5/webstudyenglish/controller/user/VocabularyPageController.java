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

import jakarta.servlet.http.HttpSession;

@Controller
public class VocabularyPageController {

    @Autowired
    private VocabularyService vocabularyService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/vocabularies")
    public String getTopics(Model model, HttpSession session) {
        List<Category> topics = vocabularyService.getVocabularyTopics();
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
        
        List<Vocabulary> flashcards;
        if (categoryId != null) {
            flashcards = vocabularyService.getFlashcards(categoryId);
            Category category = categoryService.findById(categoryId);
            model.addAttribute("category", category);
        } else {
            // Default empty or handle appropriately if no category is given
            flashcards = java.util.Collections.emptyList();
        }
        
        model.addAttribute("flashcards", flashcards);
        
        Object roleIdObj = session.getAttribute("roleId");
        if (roleIdObj != null) {
            model.addAttribute("roleId", (int) roleIdObj);
        }
        
        model.addAttribute("activeMenu", "vocabulary");
        model.addAttribute("userPath", "user/vocabulary/flashcard");
        return "user/index";
    }
}
