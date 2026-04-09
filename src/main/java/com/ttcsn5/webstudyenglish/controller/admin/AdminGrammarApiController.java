package com.ttcsn5.webstudyenglish.controller.admin;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ttcsn5.webstudyenglish.entity.Category;
import com.ttcsn5.webstudyenglish.entity.Grammar;
import com.ttcsn5.webstudyenglish.repository.CategoryRepo;
import com.ttcsn5.webstudyenglish.repository.GrammarRepo;

@RestController
@RequestMapping("/admin/api/grammar")
public class AdminGrammarApiController {

    @Autowired
    private GrammarRepo grammarRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @PostMapping
    public ResponseEntity<Grammar> createOrUpdate(
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam("name") String name,
            @RequestParam("content") String content,
            @RequestParam("categoryId") Integer categoryId) {

        Category category = categoryRepo.findById(categoryId).orElseThrow();

        Grammar grammar;
        if (id != null) {
            grammar = grammarRepo.findById(id).orElseThrow();
            grammar.setUpdated_at(LocalDateTime.now());
        } else {
            grammar = new Grammar();
            grammar.setCreated_at(LocalDateTime.now());
            grammar.setUpdated_at(LocalDateTime.now());
        }

        grammar.setName(name);
        grammar.setContent(content);
        grammar.setCategory(category);

        return ResponseEntity.ok(grammarRepo.save(grammar));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        grammarRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }
}