package com.ttcsn5.webstudyenglish.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ttcsn5.webstudyenglish.repository.AccountRepo;
import com.ttcsn5.webstudyenglish.repository.ArticleRepo;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private ArticleRepo articleRepo;

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model, HttpSession session) {
        Object roleIdObj = session.getAttribute("roleId");
        if (roleIdObj == null) {
            return "redirect:/login";
        }
        int roleId = (int) roleIdObj;
        if (roleId != 1) {
            return "redirect:/login";
        }
        String username = (String) session.getAttribute("username");

        model.addAttribute("totalUser", accountRepo.countUser());
        model.addAttribute("totalArticle", articleRepo.countArticle());
        model.addAttribute("username", username.toUpperCase());
        model.addAttribute("path", "admin/dashboard");
        return "admin/adminhome";
    }

}
