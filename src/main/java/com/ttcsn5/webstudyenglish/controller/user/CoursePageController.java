package com.ttcsn5.webstudyenglish.controller.user;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.ttcsn5.webstudyenglish.entity.Article;
import com.ttcsn5.webstudyenglish.entity.Category;
import com.ttcsn5.webstudyenglish.entity.Course;
import com.ttcsn5.webstudyenglish.entity.Subscription;
import com.ttcsn5.webstudyenglish.entity.User;
import com.ttcsn5.webstudyenglish.entity.Video;
import com.ttcsn5.webstudyenglish.repository.ArticleRepo;
import com.ttcsn5.webstudyenglish.repository.VideoRepo;
import com.ttcsn5.webstudyenglish.service.CourseService;
import com.ttcsn5.webstudyenglish.service.SubscriptionService;
import com.ttcsn5.webstudyenglish.service.VideoService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CoursePageController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private ArticleRepo articleRepo;

    @Autowired
    private VideoRepo videoRepo;

    @Autowired
    private SubscriptionService subscriptionService;

    // @GetMapping("/courses")
    // public String courses(@RequestParam(value = "keyword", required = false,
    // defaultValue = "") String keyword,
    // Model model, HttpSession session) {
    // model.addAttribute("courses", keyword.isBlank() ?
    // courseService.findPublished()
    // : courseService.search(keyword).stream().filter(course ->
    // Boolean.TRUE.equals(course.getStatus())).toList());
    // model.addAttribute("keyword", keyword);
    // model.addAttribute("activeMenu", "course");
    // model.addAttribute("userPath", "user/course/index");
    // addRole(session, model);
    // return "user/index";
    // }

    // @GetMapping("/courses/{id}")
    // public String courseDetail(@PathVariable("id") Integer id, Model model,
    // HttpSession session) {
    // Course course = courseService.findById(id);
    // List<Video> videos = videoService.findByCategory(id);
    // List<Article> articles =
    // articleRepo.findByCourse_IdAndStatusTrueOrderByCreatedAtDesc(id);
    // model.addAttribute("course", course);
    // model.addAttribute("videos", videos);
    // model.addAttribute("articles", articles);
    // model.addAttribute("activeMenu", "course");
    // model.addAttribute("userPath", "user/course/detail");
    // addRole(session, model);
    // return "user/index";
    // }

    @GetMapping("/videos")
    public String videos(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "categorySearch", required = false, defaultValue = "0") Integer categorySearch,
            @RequestParam(name = "cnt", required = false, defaultValue = "0") Integer cnt,

            Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRoleId().getCode().equals("USER")) {
            return "redirect:/login";
        }

        Set<Subscription> subscriptions = subscriptionService.getSubscriptionRepobyUserId(user.getId());

        Set<Category> categories = subscriptions.stream().map(subscription -> subscription.getPlan().getVideos())
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        Pageable pageable = PageRequest.of(cnt, 12, Sort.by("createdAt").descending());

        model.addAttribute("videos",
                videoRepo.findVideoUserHomeAndCategoryPlan(pageable, keyword, categorySearch, categories));
        model.addAttribute("keyword", keyword);
        model.addAttribute("activeMenu", "video");
        model.addAttribute("userPath", "user/video/index");
        model.addAttribute("categories", categories);
        model.addAttribute("categorySearch", categorySearch);
        model.addAttribute("cnt", cnt);
        addRole(session, model);
        return "user/index";
    }

    @GetMapping("/videos/{id}")
    public String videoDetail(@PathVariable("id") Integer id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRoleId().getCode().equals("USER")) {
            return "redirect:/login";
        }
        Video video = videoService.findById(id);
        List<Video> relatedVideos = video != null && video.getCategory() != null
                ? videoService.findByCategory(video.getCategory().getId())
                : videoService.findPublished();
        model.addAttribute("video", video);
        model.addAttribute("relatedVideos", relatedVideos);
        model.addAttribute("activeMenu", "video");
        model.addAttribute("userPath", "user/video/detail");
        addRole(session, model);
        return "user/index";
    }

    private void addRole(HttpSession session, Model model) {
        Object roleIdObj = session.getAttribute("roleId");
        if (roleIdObj != null) {
            model.addAttribute("roleId", (int) roleIdObj);
        }
    }
}