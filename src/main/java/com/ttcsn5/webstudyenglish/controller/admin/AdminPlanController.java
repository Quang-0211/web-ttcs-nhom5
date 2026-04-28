package com.ttcsn5.webstudyenglish.controller.admin;

import java.util.Locale.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ttcsn5.webstudyenglish.entity.Plan;
import com.ttcsn5.webstudyenglish.service.CategoryService;
import com.ttcsn5.webstudyenglish.service.PlanService;

@Controller
@RequestMapping("/admin/plans")
public class AdminPlanController {
    @Autowired
    private PlanService planService;
    @Autowired
    private CategoryService cateService;

    @GetMapping
    public String listPlans(@RequestParam(value = "cnt", required = false, defaultValue = "0") Integer cntString,
            Model model) {

        model.addAttribute("cnt", cntString);
        model.addAttribute("plans", planService.getAllPlans());
        model.addAttribute("path", "admin/plan");

        
        model.addAttribute("allArticles", cateService.findAllArticle());
        model.addAttribute("allVideos", cateService.findAllVideo());
        model.addAttribute("allQuizzes", cateService.findAllQuiz());
        model.addAttribute("allGrammars", cateService.findAllGrammar());
        model.addAttribute("allVocabularies", cateService.findAllVocabulary());
        model.addAttribute("allDictations", cateService.findAllDictation());
        return "admin/adminhome";
    }

}
