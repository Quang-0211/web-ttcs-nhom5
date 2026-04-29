package com.ttcsn5.webstudyenglish.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ttcsn5.webstudyenglish.dto.request.UserRequest;
import com.ttcsn5.webstudyenglish.entity.Plan;
import com.ttcsn5.webstudyenglish.entity.Role;
import com.ttcsn5.webstudyenglish.entity.Subscription;
import com.ttcsn5.webstudyenglish.entity.User;
import com.ttcsn5.webstudyenglish.service.AccountService;
import com.ttcsn5.webstudyenglish.service.HashPassword;
import com.ttcsn5.webstudyenglish.service.PlanService;
import com.ttcsn5.webstudyenglish.service.RoleService;
import com.ttcsn5.webstudyenglish.service.SubscriptionService;
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
    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private SubscriptionService subService;
    @Autowired
    private PlanService planService;

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
        System.out.println(email + " " + password + " " + username + " " +
                ase.existsByEmail(email));
        RegisterStatus registerStatus = ase.checkRegister(username, email);
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
        Role role = rse.findByCode("USER");
        User user = new User(username, hashPassword.hashPassword(password), email, role);

        Plan plan = planService.getByName("user-base");
        ase.saveUser(user);

        subscriptionService.saveSubscription(Subscription.builder()
                .user(user)
                .plan(plan)
                .transactionId("Base-" + System.currentTimeMillis())
                .startDate(java.time.LocalDateTime.now())
                .endDate(java.time.LocalDateTime.now().plusYears(100))
                .active(true)
                .build());
        redirectAttribute.addFlashAttribute("success", "Tao tai khoan thanh cong");
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String authenLogin(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model,
            HttpSession session) {
        UserRequest userRequest = new UserRequest(email, password);
        LoginResponse loginStatus = ase.checkLogin(userRequest);
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
        session.setAttribute("user", user);
        if (user.getRoleId().getCode().equals("ADMIN")) {

            return "redirect:/admin/dashboard";
        }
        Set<Subscription> subscriptions = subscriptionService.getSubscriptionRepobyUserId(user.getId());
        session.setAttribute("subscriptions", subscriptions);
        return "redirect:/home";

    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model, jakarta.servlet.http.HttpServletResponse response) {

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }

        model.addAttribute("activeMenu", "home");
        model.addAttribute("userPath", "user/home");
        return "user/index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
