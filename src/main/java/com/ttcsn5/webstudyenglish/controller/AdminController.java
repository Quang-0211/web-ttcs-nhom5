package com.ttcsn5.webstudyenglish.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AdminController {
    // @GetMapping("/admin/{path}")
    // public String redirect(@PathVariable("path") String path, Model model) {
    // List<String> allowedPaths = Arrays.asList("dashboard", "user", "categories",
    // "lessons", "article");
    // if (!allowedPaths.contains(path)) {
    // return "error/404";
    // }
    // model.addAttribute("path", "admin/" + path);
    // model.addAttribute("current", path);
    // return "admin/adminhome";
    // }
}
