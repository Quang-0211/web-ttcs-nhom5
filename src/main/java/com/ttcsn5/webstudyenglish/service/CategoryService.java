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

    public List<Category> findAll() {
        return cateRepo.findAll();
    }

    public Category findById(int id) {
        return cateRepo.findById(id).orElse(null);
    }
}
