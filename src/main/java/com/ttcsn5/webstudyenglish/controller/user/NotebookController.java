package com.ttcsn5.webstudyenglish.controller.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ttcsn5.webstudyenglish.entity.UserVocabulary;
import com.ttcsn5.webstudyenglish.entity.Vocabulary;
import com.ttcsn5.webstudyenglish.service.UserVocabularyService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/notebook")
public class NotebookController {

    @Autowired
    private UserVocabularyService userVocabularyService;

    @GetMapping
    public String getNotebook(Model model, HttpSession session) {
        Object userIdObj = session.getAttribute("userId");
        if (userIdObj == null) {
            return "redirect:/login";
        }

        Long userId = Long.valueOf(userIdObj.toString());
        List<UserVocabulary> savedUserVocabs = userVocabularyService.getSavedVocabularies(userId);
        
        List<Vocabulary> savedVocabularies = savedUserVocabs.stream()
                .map(UserVocabulary::getVocabulary)
                .collect(Collectors.toList());

        model.addAttribute("savedVocabularies", savedVocabularies);
        model.addAttribute("activeMenu", "notebook");
        model.addAttribute("userPath", "user/notebook/index");
        
        Object roleIdObj = session.getAttribute("roleId");
        if (roleIdObj != null) {
            model.addAttribute("roleId", (int) roleIdObj);
        }

        return "user/index";
    }
}
