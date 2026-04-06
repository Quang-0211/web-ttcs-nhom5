package com.ttcsn5.webstudyenglish.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ttcsn5.webstudyenglish.repository.CategoryRepo;
import com.ttcsn5.webstudyenglish.repository.VocabularyRepo;

@Controller
public class AdminController {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private VocabularyRepo vocabularyRepo;

    @GetMapping("/admin/{path}")
    public String redirect(@PathVariable("path") String path, Model model, jakarta.servlet.http.HttpSession session,
            jakarta.servlet.http.HttpServletResponse response) {

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        List<String> allowedPaths = Arrays.asList("dashboard", "user", "categories",
                "lessons", "article", "vocabulary");
        if (!allowedPaths.contains(path)) {
            return "error/404";
        }

        if ("vocabulary".equals(path)) {
            model.addAttribute("categories", categoryRepo.findAll());
            model.addAttribute("vocabularies", vocabularyRepo.findAll());
        }

        model.addAttribute("path", "admin/" + path);
        model.addAttribute("current", path);
        return "admin/adminhome";
    }
}
