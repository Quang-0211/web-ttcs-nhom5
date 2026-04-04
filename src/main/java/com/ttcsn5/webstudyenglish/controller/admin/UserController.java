package com.ttcsn5.webstudyenglish.controller.admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ttcsn5.webstudyenglish.entity.User;
import com.ttcsn5.webstudyenglish.service.AccountService;
import com.ttcsn5.webstudyenglish.service.RoleService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
    @Autowired
    private AccountService ase;
    @Autowired
    private RoleService rse;

    @GetMapping("/admin/users")
    public String user(@RequestParam(value = "cnt", required = false, defaultValue = "0") String cntString,
            @RequestParam(name = "username", required = false, defaultValue = "") String username,
            @RequestParam(name = "email", required = false, defaultValue = "") String email,
            @RequestParam(name = "role", required = false, defaultValue = "0") String roleUser,
            Model model, HttpSession session) {
        try {

            int roleId = (int) session.getAttribute("roleId");
            if (roleId != 1) {
                return "redirect:/login";
            }
            int cnt = Integer.parseInt(cntString);
            List<User> users = new ArrayList<>();

            // if (action.equals("search")) {

            users = ase.searchUser(username, email, Integer.parseInt(roleUser), cnt);
            // } else {
            // users = ase.findByCnt(cnt);
            // }
            model.addAttribute("username", username);
            model.addAttribute("email", email);
            model.addAttribute("roleSelected", Integer.parseInt(roleUser));
            model.addAttribute("users", users);
            model.addAttribute("path", "admin/user");
            model.addAttribute("cnt", cnt);
            model.addAttribute("roles", rse.findAll());
            return "admin/adminhome";
        } catch (Exception e) {
            // TODO: handle exception
            return "redirect:/admin/users";
        }
    }
}
