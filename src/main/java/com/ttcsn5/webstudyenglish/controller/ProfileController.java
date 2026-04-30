package com.ttcsn5.webstudyenglish.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ttcsn5.webstudyenglish.entity.Subscription;
import com.ttcsn5.webstudyenglish.entity.User;
import com.ttcsn5.webstudyenglish.service.SubscriptionService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class ProfileController {

    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model, HttpServletResponse response) {
        setNoCacheHeaders(response);

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        String roleCode = user.getRoleId() != null ? user.getRoleId().getCode() : "";

        if ("ADMIN".equalsIgnoreCase(roleCode)) {
            model.addAttribute("path", "shared/profile");
            model.addAttribute("current", "profile");
            return "admin/adminhome";
        }

        Set<Subscription> subscriptions = subscriptionService.getSubscriptionRepobyUserId(user.getId());
        model.addAttribute("subscriptions", subscriptions);
        model.addAttribute("activeMenu", "profile");
        model.addAttribute("userPath", "shared/profile");
        return "user/index";
    }

    private void setNoCacheHeaders(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }
}
