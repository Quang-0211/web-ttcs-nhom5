package com.ttcsn5.webstudyenglish.controller.admin;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ttcsn5.webstudyenglish.entity.Article;
import com.ttcsn5.webstudyenglish.service.ArticleService;
import com.ttcsn5.webstudyenglish.service.CategoryService;
import com.ttcsn5.webstudyenglish.service.CourseService;
import com.ttcsn5.webstudyenglish.service.UploadImageAudio;

import jakarta.servlet.http.HttpSession;

@Controller
public class ArticleController {
    @Autowired
    private CategoryService cateService;
    @Autowired
    private ArticleService artService;
    @Autowired
    private UploadImageAudio uploadService;
    @Autowired
    private CourseService courseService;

    @GetMapping("/admin/articles")
    public String articles(@RequestParam(value = "cnt", required = false, defaultValue = "0") Integer cntString,
            @RequestParam(name = "title", required = false, defaultValue = "") String title,
            @RequestParam(name = "category", required = false, defaultValue = "0") Integer category,
            @RequestParam(name = "status", required = false, defaultValue = "true") Boolean status,
            Model model, HttpSession session) {
        Integer roleId = (Integer) session.getAttribute("roleId");
        if (roleId == null || roleId != 1) {
            return "redirect:/login";
        }
        List<Article> articles = artService.searchArticle(title, category, status, cntString);
        model.addAttribute("title", title);
        model.addAttribute("categorySelected", category);
        model.addAttribute("statusSelected", status);
        model.addAttribute("cnt", cntString);
        model.addAttribute("categories", cateService.findAllArticle());
        model.addAttribute("courses", courseService.findPublished());
        model.addAttribute("articles", articles);
        model.addAttribute("path", "admin/article");
        return "admin/adminhome";
    }

    @PostMapping("/admin/articles")
    public String doPost(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(name = "image", required = false) MultipartFile image,
            @RequestParam(name = "audio", required = false) MultipartFile audio,
            @RequestParam(name = "articleId", required = false, defaultValue = "") String articleId,
            @RequestParam(name = "status", required = false, defaultValue = "true") boolean status,
            @RequestParam("category") Integer categoryId,
            @RequestParam(name = "courseId", required = false, defaultValue = "0") Integer courseId) throws IOException {

        System.out.println(articleId + " " + title + " " + content);
        String imgPathStr = uploadService.upload(image, "images");
        String audPathStr = uploadService.upload(audio, "audios");

        Article article = null;
        System.out.println(articleId + " " + title + " " + content);
        var course = courseId != null && courseId > 0 ? courseService.findById(courseId) : null;
        if (articleId.equals("")) {

            article = new Article(title, content, imgPathStr, audPathStr, status,
                cateService.findById(categoryId), course);
        } else {
            article = new Article(Integer.parseInt(articleId), title, content, imgPathStr, audPathStr, status,
                cateService.findById(categoryId), course);
        }
        artService.save(article);

        return "redirect:/admin/articles";
    }

    @PostMapping("/admin/articles/publish")
    @ResponseBody
    public java.util.Map<String, Object> updateStatus(@RequestParam("articleId") String articleId,
            @RequestParam("status") String status) {
        try {
            artService.updateStatus(Integer.parseInt(articleId), Boolean.parseBoolean(status));
            return java.util.Map.of("success", true, "message", "Article status updated successfully");
        } catch (Exception e) {
            return java.util.Map.of("success", false, "message", "Error updating article status");
        }
    }

    @PostMapping("/admin/articles/delete")
    @ResponseBody
    public java.util.Map<String, Object> delete(@RequestParam("articleId") Integer articleId) {
        try {
            artService.delete(articleId);
            return java.util.Map.of("success", true, "message", "Article deleted successfully");
        } catch (Exception e) {
            return java.util.Map.of("success", false, "message", "Error deleting article");
        }
    }
}
