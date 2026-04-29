package com.ttcsn5.webstudyenglish.controller.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.ttcsn5.webstudyenglish.dto.response.ArticleDetailResponse;
import com.ttcsn5.webstudyenglish.dto.response.ArticlesUserHomeResponse;
import com.ttcsn5.webstudyenglish.entity.Category;
import com.ttcsn5.webstudyenglish.entity.User;
import com.ttcsn5.webstudyenglish.repository.ArticleRepo;
import com.ttcsn5.webstudyenglish.repository.CategoryRepo;
import com.ttcsn5.webstudyenglish.service.CategoryService;

import jakarta.servlet.http.HttpSession;

@Controller
public class Articles {
    @Autowired
    private CategoryRepo cateRepo;
    @Autowired
    private CategoryService cateSer;
    @Autowired
    private ArticleRepo articleRepo;

    @GetMapping("/user/articles")
    public String home(
            @RequestParam(name = "cnt", required = false, defaultValue = "0") Integer cnt,
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "categorySearch", required = false, defaultValue = "0") Integer categorySearch,
            Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRoleId().getCode().equals("USER")) {
            return "redirect:/login";
        }
        List<Category> categorys = cateSer.findAllNameCate("Article");

        Pageable pageable = PageRequest.of(cnt, 12, Sort.by("createdAt").descending());

        model.addAttribute("articles", articleRepo.findArticleUserHome(pageable, keyword, categorySearch));
        model.addAttribute("categorys", categorys);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categorySearch", categorySearch);
        model.addAttribute("cnt", cnt);
        model.addAttribute("activeMenu", "articles");
        model.addAttribute("userPath", "user/articles");

        return "user/index";
    }

    @GetMapping("/user/article/{id}")
    public String articleDetail(@PathVariable("id") Integer id,
            Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRoleId().getCode().equals("USER")) {
            return "redirect:/login";
        }

        ArticleDetailResponse article = articleRepo.findArticleDetail(id);
        Pageable pageable = PageRequest.of(0, 5, Sort.by("createdAt").descending());
        System.out.println("cate id : " + article.getCateId());
        Category cate = cateRepo.findById(article.getCateId()).get();
        Page<ArticlesUserHomeResponse> page = articleRepo.findArticleUserHome(pageable, "",
                cate != null ? cate.getId() : 0);
        model.addAttribute("article", article);
        model.addAttribute("articlesRelated", page);
        model.addAttribute("activeMenu", "article");
        return "user/article-detail";
    }
}
