package com.ttcsn5.webstudyenglish.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ttcsn5.webstudyenglish.entity.Role;
import com.ttcsn5.webstudyenglish.entity.User;
import com.ttcsn5.webstudyenglish.service.AccountService;
import com.ttcsn5.webstudyenglish.service.HashPassword;
import com.ttcsn5.webstudyenglish.service.RoleService;
import com.ttcsn5.webstudyenglish.totalenum.LoginResponse;
import com.ttcsn5.webstudyenglish.totalenum.RegisterStatus;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {
    @Autowired
    private AccountService ase;
    @Autowired
    private RoleService rse;
    @Autowired
    private HashPassword hashPassword;

    @GetMapping(path = { "/", "/login" })
    public String login(Model model) {

        return "login";
    }

    @GetMapping("/register")
    public String register() {

        return "register";
    }

    @PostMapping("/register")
    public String auth(@RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            RedirectAttributes redirectAttribute,
            Model model) {
        // System.out.println(email + " " + password + " " + username + " " +
        // ase.existsByEmail(email));
        RegisterStatus registerStatus = ase.checkRegister(username, email, password);
        switch (registerStatus) {
            case EMAIL_EXISTS:
                model.addAttribute("error", "Kiem tra lai Email");
                model.addAttribute("username", username);
                return "register";
            case USERNAME_EXISTS:
                model.addAttribute("error", "Kiem tra lai Username");
                model.addAttribute("username", username);
                return "register";
            case SUCCESS:
                break;
        }

        Role role = rse.findByCode("ADMIN");
        ase.saveUser(new User(username, hashPassword.hashPassword(password), email, role));
        redirectAttribute.addFlashAttribute("success", "Tao tai khoan thanh cong");
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String authenLogin(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model,
            HttpSession session) {
        LoginResponse loginStatus = ase.checkLogin(email, password);
        switch (loginStatus.getStatus()) {
            case INVALID_EMAIL:
                model.addAttribute("errorLogin", "1");
                model.addAttribute("email", email);
                model.addAttribute("password", password);
                return "login";
            case INVALID_PASSWORD:
                model.addAttribute("errorLogin", "2");
                model.addAttribute("email", email);
                model.addAttribute("password", password);
                return "login";
            case SUCCESS:
                break;
        }
        User user = loginStatus.getUser();
        if (user.getRoleId().getCode().equals("ADMIN")) {
            session.setAttribute("userId", user.getId());
            session.setAttribute("roleId", user.getRoleId().getId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("email", user.getEmail());
            return "redirect:/admin/dashboard";
        }
        return "redirect:/home";

    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model, jakarta.servlet.http.HttpServletResponse response) {

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        Object roleIdObj = session.getAttribute("roleId");
        if (roleIdObj != null) {
            model.addAttribute("roleId", (int) roleIdObj);
        }
        return "user/home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
