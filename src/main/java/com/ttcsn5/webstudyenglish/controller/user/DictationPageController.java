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
import com.ttcsn5.webstudyenglish.entity.DictationSentences;
import com.ttcsn5.webstudyenglish.entity.DictationTopics;
import com.ttcsn5.webstudyenglish.service.CategoryService;
import com.ttcsn5.webstudyenglish.service.DictationService;
import com.ttcsn5.webstudyenglish.entity.Subscription;
import com.ttcsn5.webstudyenglish.entity.User;
import com.ttcsn5.webstudyenglish.service.SubscriptionService;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/dictation")
public class DictationPageController {

    @Autowired
    private DictationService dictationService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubscriptionService subscriptionService;

    private void addCommonAttributes(Model model, HttpSession session, String userPath) {
        model.addAttribute("activeMenu", "dictation");
        model.addAttribute("userPath", userPath);
        Object roleIdObj = session.getAttribute("roleId");
        if (roleIdObj != null) {
            model.addAttribute("roleId", (int) roleIdObj);
        }
    }

    @GetMapping
    public String getCategories(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        Set<Subscription> subscriptions = subscriptionService.getSubscriptionRepobyUserId(user.getId());
        Set<Category> categories = subscriptions.stream().map(subscription -> subscription.getPlan().getDictationTopics())
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
        model.addAttribute("categories", categories);
        addCommonAttributes(model, session, "user/dictation/categories");
        return "user/index";
    }

    @GetMapping("/category/{id}")
    public String getTopicsByCategory(
            @PathVariable("id") Integer categoryId,
            @RequestParam(value = "search", required = false) String search,
            Model model,
            HttpSession session) {
        
        Category category = categoryService.findById(categoryId);
        List<DictationTopics> topics = dictationService.getTopicsByCategoryId(categoryId, search);
        
        model.addAttribute("category", category);
        model.addAttribute("topics", topics);
        model.addAttribute("search", search);
        
        addCommonAttributes(model, session, "user/dictation/topics");
        return "user/index";
    }

    @GetMapping("/{id}")
    public String getPracticePage(@PathVariable("id") Integer id, Model model, HttpSession session) {
        DictationTopics topic = dictationService.getTopicById(id);
        List<DictationSentences> sentences = dictationService.getSentencesByTopicId(id);
        
        model.addAttribute("topic", topic);
        model.addAttribute("sentences", sentences);
        
        addCommonAttributes(model, session, "user/dictation/practice");
        return "user/index";
    }
}
