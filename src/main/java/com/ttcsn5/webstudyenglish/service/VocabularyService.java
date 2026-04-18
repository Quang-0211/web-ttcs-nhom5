package com.ttcsn5.webstudyenglish.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ttcsn5.webstudyenglish.entity.Category;
import com.ttcsn5.webstudyenglish.entity.Vocabulary;
import com.ttcsn5.webstudyenglish.repository.CategoryRepo;
import com.ttcsn5.webstudyenglish.repository.VocabularyRepo;

@Service
public class VocabularyService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private VocabularyRepo vocabularyRepo;

    public List<Category> getVocabularyTopics() {
        return categoryRepo.findByParentNameIgnoreCase("vocab");
    }

    public List<Vocabulary> getVocabulariesByCategory(Integer categoryId) {
        return vocabularyRepo.findByCategoryId(categoryId);
    }

    public List<Vocabulary> searchVocabularies(Integer categoryId, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getVocabulariesByCategory(categoryId);
        }
        return vocabularyRepo.findByCategoryIdAndWordContainingIgnoreCase(categoryId, keyword.trim());
    }

    public Vocabulary getVocabularyById(Integer id) {
        return vocabularyRepo.findById(id).orElse(null);
    }

    public List<Vocabulary> getFlashcards(Integer categoryId) {
        // Here we just return the vocabularies for a category to use as flashcards
        return vocabularyRepo.findByCategoryId(categoryId);
    }
}
