package com.ttcsn5.webstudyenglish.controller.admin;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ttcsn5.webstudyenglish.repository.CategoryRepo;
import com.ttcsn5.webstudyenglish.repository.VocabularyRepo;
import com.ttcsn5.webstudyenglish.repository.GrammarRepo;
import com.ttcsn5.webstudyenglish.repository.DictationTopicRepo;

@Controller
public class AdminController {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private VocabularyRepo vocabularyRepo;

    @Autowired
    private GrammarRepo grammarRepo;

    @Autowired
    private DictationTopicRepo dictationTopicRepo;

    @GetMapping("/admin/{path}")
    public String redirect(@PathVariable("path") String path, Model model, jakarta.servlet.http.HttpSession session,
            jakarta.servlet.http.HttpServletResponse response) {

        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        List<String> allowedPaths = Arrays.asList("dashboard", "user", "categories",
                "lessons", "article", "vocabulary", "grammar", "dictation", "quiz");
        if (!allowedPaths.contains(path)) {
            return "error/404";
        }

        if ("vocabulary".equals(path)) {
            model.addAttribute("categories", categoryRepo.findAll());
            model.addAttribute("vocabularies", vocabularyRepo.findAll());
        }

        if ("grammar".equals(path)) {
            model.addAttribute("categories", categoryRepo.findAll());
            model.addAttribute("grammars", grammarRepo.findAll());
        }

        if ("dictation".equals(path)) {
            model.addAttribute("categories", categoryRepo.findAll());
            model.addAttribute("dictationTopics", dictationTopicRepo.findAll());
        }

        if ("quiz".equals(path)) {
            model.addAttribute("categories", categoryRepo.findAll());
        }

        if (Arrays.asList("dictation", "grammar", "vocabulary", "quiz").contains(path)) {
            model.addAttribute("path", "admin/" + path + "/index");
        } else {
            model.addAttribute("path", "admin/" + path);
        }

        model.addAttribute("current", path);
        return "admin/adminhome";
    }

    @GetMapping("/admin/quiz/create")
    public String createQuiz(Model model, jakarta.servlet.http.HttpSession session,
            jakarta.servlet.http.HttpServletResponse response) {

        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        model.addAttribute("categories", categoryRepo.findAll());
        model.addAttribute("path", "admin/quiz/create");
        model.addAttribute("current", "quiz");
        return "admin/adminhome";
    }

    @GetMapping("/admin/quiz/edit/{id}")
    public String editQuiz(@PathVariable("id") Integer id, Model model, jakarta.servlet.http.HttpSession session,
            jakarta.servlet.http.HttpServletResponse response) {

        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        model.addAttribute("categories", categoryRepo.findAll());
        model.addAttribute("path", "admin/quiz/edit");
        model.addAttribute("current", "quiz");
        return "admin/adminhome";
    }
}
