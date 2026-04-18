package com.ttcsn5.webstudyenglish.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ttcsn5.webstudyenglish.entity.Category;
import com.ttcsn5.webstudyenglish.entity.Grammar;
import com.ttcsn5.webstudyenglish.repository.CategoryRepo;
import com.ttcsn5.webstudyenglish.repository.GrammarRepo;

@Service
public class GrammarService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private GrammarRepo grammarRepo;

    public List<Category> getGrammarTopics() {
        return categoryRepo.findByParentNameIgnoreCase("Grammar");
    }

    public List<Grammar> getGrammarsByCategory(Integer categoryId) {
        return grammarRepo.findByCategoryId(categoryId);
    }

    public List<Grammar> searchGrammars(Integer categoryId, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getGrammarsByCategory(categoryId);
        }
        return grammarRepo.findByCategoryIdAndNameContainingIgnoreCase(categoryId, keyword.trim());
    }

    public Grammar getGrammarById(Integer id) {
        return grammarRepo.findById(id).orElse(null);
    }
}
