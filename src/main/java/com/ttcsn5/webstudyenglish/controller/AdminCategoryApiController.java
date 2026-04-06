package com.ttcsn5.webstudyenglish.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ttcsn5.webstudyenglish.entity.Category;
import com.ttcsn5.webstudyenglish.entity.User;
import com.ttcsn5.webstudyenglish.repository.AccountRepo;
import com.ttcsn5.webstudyenglish.repository.CategoryRepo;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/admin/api/category")
public class AdminCategoryApiController {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private AccountRepo accountRepo;

    @PostMapping
    public ResponseEntity<Category> create(
            @RequestParam("name") String name,
            @RequestParam(value = "parentId", required = false) Integer parentId,
            HttpSession session) {
        
        Long userId = (Long) session.getAttribute("userId");
        User currentUser = null;
        if (userId != null) {
            currentUser = accountRepo.findById(userId).orElse(null);
        }

        Category category = new Category();
        category.setName(name);
        
        if (currentUser != null) {
            category.setCreatedBy(currentUser);
            category.setUpdatedBy(currentUser);
        }
        
        if (parentId != null) {
            Category parent = categoryRepo.findById(parentId).orElse(null);
            if (parent != null && parent.getParent() != null) {
                return ResponseEntity.badRequest().build();
            }
            category.setParent(parent);
        }
        
        return ResponseEntity.ok(categoryRepo.save(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> update(
            @PathVariable("id") Integer id,
            @RequestParam("name") String name,
            HttpSession session) {
        
        Long userId = (Long) session.getAttribute("userId");
        User currentUser = null;
        if (userId != null) {
            currentUser = accountRepo.findById(userId).orElse(null);
        }
        
        Category category = categoryRepo.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        category.setName(name);
        
        if (currentUser != null) {
            category.setUpdatedBy(currentUser);
        }
        
        return ResponseEntity.ok(categoryRepo.save(category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        try {
            categoryRepo.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Cannot delete category.");
        }
    }
}
