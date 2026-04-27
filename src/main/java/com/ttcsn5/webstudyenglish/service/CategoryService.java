package com.ttcsn5.webstudyenglish.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ttcsn5.webstudyenglish.entity.Category;
import com.ttcsn5.webstudyenglish.repository.CategoryRepo;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepo cateRepo;

    public List<Category> findAllArticle() {
        Category cate = cateRepo.findAllByName("Article");
        return cateRepo.findByParent(cate);
    }

    public List<Category> findAllVideo() {
        Category cate = cateRepo.findAllByName("Video");
        return cateRepo.findByParent(cate);
    }

    public List<Category> findAllQuiz() {
        Category cate = cateRepo.findAllByName("Quiz");
        return cateRepo.findByParent(cate);
    }

    public Category findById(int id) {
        return cateRepo.findById(id).orElse(null);
    }
}
