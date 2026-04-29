package com.ttcsn5.webstudyenglish.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.ttcsn5.webstudyenglish.entity.Article;
import com.ttcsn5.webstudyenglish.entity.Course;
import com.ttcsn5.webstudyenglish.entity.User;
import com.ttcsn5.webstudyenglish.entity.Video;
import com.ttcsn5.webstudyenglish.repository.ArticleRepo;
import com.ttcsn5.webstudyenglish.service.CourseService;
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
    public String videos(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRoleId().getCode().equals("USER")) {
            return "redirect:/login";
        }
        model.addAttribute("videos", keyword.isBlank() ? videoService.findPublished()
                : videoService.search(keyword).stream().filter(video -> Boolean.TRUE.equals(video.getStatus()))
                        .toList());
        model.addAttribute("keyword", keyword);
        model.addAttribute("activeMenu", "video");
        model.addAttribute("userPath", "user/video/index");
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