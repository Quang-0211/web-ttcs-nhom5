package com.ttcsn5.webstudyenglish.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ttcsn5.webstudyenglish.entity.Category;
import com.ttcsn5.webstudyenglish.entity.DictationSentences;
import com.ttcsn5.webstudyenglish.entity.DictationTopics;
import com.ttcsn5.webstudyenglish.repository.CategoryRepo;
import com.ttcsn5.webstudyenglish.repository.DictationSentenceRepo;
import com.ttcsn5.webstudyenglish.repository.DictationTopicRepo;

@Service
public class DictationService {

    @Autowired
    private DictationTopicRepo dictationTopicRepo;
    
    @Autowired
    private DictationSentenceRepo dictationSentenceRepo;
    
    @Autowired
    private CategoryRepo categoryRepo;

    public List<Category> getDictationCategories() {
        return categoryRepo.findByParentNameIgnoreCase("dictation");
    }

    public List<DictationTopics> getTopicsByCategoryId(Integer categoryId, String search) {
        if (search != null && !search.trim().isEmpty()) {
            return dictationTopicRepo.findByCategory_IdAndTitleContainingIgnoreCase(categoryId, search.trim());
        }
        return dictationTopicRepo.findByCategory_Id(categoryId);
    }
    
    public DictationTopics getTopicById(Integer id) {
        return dictationTopicRepo.findById(id).orElse(null);
    }
    
    public List<DictationSentences> getSentencesByTopicId(Integer topicId) {
        DictationTopics topic = getTopicById(topicId);
        if (topic != null) {
            return dictationSentenceRepo.findByTopic(topic);
        }
        return List.of();
    }
}
