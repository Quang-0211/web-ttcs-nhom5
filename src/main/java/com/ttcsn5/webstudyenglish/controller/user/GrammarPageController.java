package com.ttcsn5.webstudyenglish.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ttcsn5.webstudyenglish.entity.Category;
import com.ttcsn5.webstudyenglish.entity.Grammar;
import com.ttcsn5.webstudyenglish.repository.CategoryRepo;
import com.ttcsn5.webstudyenglish.service.GrammarService;

@Controller
@RequestMapping("/grammar")
public class GrammarPageController {

    @Autowired
    private GrammarService grammarService;

    @Autowired
    private CategoryRepo categoryRepo;

    @GetMapping
    public String viewTopics(Model model) {
        List<Category> topics = grammarService.getGrammarTopics();
        model.addAttribute("topics", topics);
        model.addAttribute("activeMenu", "grammar");
        model.addAttribute("userPath", "user/grammar/topics");
        return "user/index";
    }

    @GetMapping("/category/{categoryId}")
    public String viewGrammarList(
            @PathVariable Integer categoryId,
            @RequestParam(required = false) String search,
            Model model) {
        
        Category category = categoryRepo.findById(categoryId).orElse(null);
        List<Grammar> grammars = grammarService.searchGrammars(categoryId, search);

        model.addAttribute("category", category);
        model.addAttribute("grammars", grammars);
        model.addAttribute("search", search);

        model.addAttribute("activeMenu", "grammar");
        model.addAttribute("userPath", "user/grammar/list");
        return "user/index";
    }

    @GetMapping("/{id}")
    public String viewGrammarDetail(@PathVariable Integer id, Model model) {
        Grammar grammar = grammarService.getGrammarById(id);
        model.addAttribute("grammar", grammar);
        model.addAttribute("activeMenu", "grammar");
        model.addAttribute("userPath", "user/grammar/detail");
        return "user/index";
    }
}
