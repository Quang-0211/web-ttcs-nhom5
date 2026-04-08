package com.ttcsn5.webstudyenglish.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

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

        model.addAttribute("username", username.toUpperCase());
        model.addAttribute("path", "admin/dashboard");
        return "admin/adminhome";
    }

}
