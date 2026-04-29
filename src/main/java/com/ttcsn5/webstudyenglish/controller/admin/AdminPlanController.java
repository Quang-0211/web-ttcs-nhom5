package com.ttcsn5.webstudyenglish.controller.admin;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ttcsn5.webstudyenglish.entity.Plan;
import com.ttcsn5.webstudyenglish.repository.PlanRepository;
import com.ttcsn5.webstudyenglish.service.CategoryService;
import com.ttcsn5.webstudyenglish.service.PlanService;

@Controller
@RequestMapping("/admin/plans")
public class AdminPlanController {
    @Autowired
    private PlanService planService;
    @Autowired
    private CategoryService cateService;
    @Autowired
    private PlanRepository planRepo;

    @GetMapping
    public String listPlans(@RequestParam(name = "cnt", required = false, defaultValue = "0") Integer cnt,
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            @RequestParam(name = "active", required = false) Boolean status,
            @RequestParam(name = "maxPrice", required = false, defaultValue = "0") Double price,
            Model model) {

        model.addAttribute("cnt", cnt);
        model.addAttribute("plans", planService.searchPlans(name, status, price, cnt));
        model.addAttribute("path", "admin/plan");

        model.addAttribute("allArticles", cateService.findAllNameCate("Article"));
        model.addAttribute("allVideos", cateService.findAllNameCate("Video"));
        model.addAttribute("allQuizzes", cateService.findAllNameCate("Quiz"));
        model.addAttribute("allGrammars", cateService.findAllNameCate("Grammar"));
        model.addAttribute("allVocabularies", cateService.findAllNameCate("Vocab"));
        model.addAttribute("allDictations", cateService.findAllNameCate("Dictation"));

        System.out.println("set : "+planService.searchPlans(name, status, price, cnt).get(0).getDictationTopics());

        model.addAttribute("name", name);
        model.addAttribute("active", status);
        model.addAttribute("maxPrice", price);

        return "admin/adminhome";
    }

    @PostMapping
    public String createPlan(
            @RequestParam("id") Integer id,
            @RequestParam("name") String name,
            @RequestParam("price") Double price,
            @RequestParam("active") Boolean active,
            @RequestParam("description") String description,
            @RequestParam("durationDays") Integer duration,

            @RequestParam(name = "articles", required = false) Set<Integer> articleIds,
            @RequestParam(name = "quizzes", required = false) Set<Integer> quizIds,
            @RequestParam(name = "videos", required = false) Set<Integer> videoIds,
            @RequestParam(name = "grammars", required = false) Set<Integer> grammarIds,
            @RequestParam(name = "vocabularies", required = false) Set<Integer> vocabularyIds,
            @RequestParam(name = "dictationTopics", required = false) Set<Integer> dictationIds) {

        Plan plan;
        System.out.println("gia tri grammarIds: " + grammarIds);
        grammarIds.forEach(i -> System.out.println(i));
        System.out.println("gia tri vocab: " + vocabularyIds);
        vocabularyIds.forEach(i -> System.out.println(i));
        System.out.println("gia tri di: " + dictationIds);
        dictationIds.forEach(i -> System.out.println(i));

        if (id == 0) {
            // CREATE
            plan = new Plan(
                    name, price, duration,
                    cateService.findPlansByIds(articleIds, "Article"),
                    cateService.findPlansByIds(quizIds, "Quiz"),
                    cateService.findPlansByIds(videoIds, "Video"),
                    cateService.findPlansByIds(dictationIds, "Dictation"),
                    cateService.findPlansByIds(grammarIds, "Grammar"),
                    cateService.findPlansByIds(vocabularyIds, "Vocab"),
                    description, true, LocalDateTime.now()
            );
        } else {
            // UPDATE ĐÚNG CÁCH
            plan = planRepo.findById(id).orElseThrow();

            plan.setName(name);
            plan.setPrice(price);
            plan.setDurationDays(duration);
            plan.setArticles(cateService.findPlansByIds(articleIds, "Article"));
            plan.setQuizzes(cateService.findPlansByIds(quizIds, "Quiz"));
            plan.setVideos(cateService.findPlansByIds(videoIds, "Video"));
            plan.setDictationTopics(cateService.findPlansByIds(dictationIds, "Dictation"));
            plan.setGrammar(cateService.findPlansByIds(grammarIds, "Grammar"));
            plan.setVocabulary(cateService.findPlansByIds(vocabularyIds, "Vocab"));
            plan.setDescription(description);
            plan.setActive(active);
            plan.setUpdated_at(LocalDateTime.now());

            // ❗ KHÔNG đụng vào created_at
        }

        planRepo.save(plan);
        System.out.println(cateService.findPlansByIds(dictationIds, "Dictation"));
        // planService.createPlan();
        planRepo.save(plan);

        return "redirect:/admin/plans";

    }

}
