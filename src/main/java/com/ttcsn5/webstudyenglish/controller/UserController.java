package com.ttcsn5.webstudyenglish.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ttcsn5.webstudyenglish.entity.User;
import com.ttcsn5.webstudyenglish.service.AccountService;

@Controller
public class UserController {
    @Autowired
    private AccountService ase;

    @GetMapping("/admin/users")
    public String user(@RequestParam(value = "cnt", required = false, defaultValue = "0") String cntString,
            Model model) {
        int cnt = Integer.parseInt(cntString);
        model.addAttribute("users", ase.findByCnt(cnt));
        model.addAttribute("path", "admin/user");
        model.addAttribute("cnt", cnt);
        return "admin/adminhome";
    }
}
