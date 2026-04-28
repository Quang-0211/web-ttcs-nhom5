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
        Category cate = cateRepo.findByName("Article");
        return cateRepo.findByParent(cate);
    }

    public List<Category> findAllVideo() {
        Category cate = cateRepo.findByName("Video");
        return cateRepo.findByParent(cate);
    }

    public List<Category> findAllQuiz() {
        Category cate = cateRepo.findByName("Quiz");
        return cateRepo.findByParent(cate);
    }

    public List<Category> findAllGrammar() {
        Category cate = cateRepo.findByName("Grammar");
        System.out.println("gramar :" + cate.getName() + " " + cate.getId());
        return cateRepo.findByParent(cate);
    }

    public List<Category> findAllVocabulary() {
        Category cate = cateRepo.findByName("Vocab");
        System.out.println("vocab :" + cate.getName() + " " + cate.getId());
        return cateRepo.findByParent(cate);
    }

    public List<Category> findAllDictation() {
        Category cate = cateRepo.findByName("Dictation");
        System.out.println("dictation :" + cate.getName() + " " + cate.getId());
        return cateRepo.findByParent(cate);
    }

    public Category findById(int id) {
        return cateRepo.findById(id).orElse(null);
    }
}
