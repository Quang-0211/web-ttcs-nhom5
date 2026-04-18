package com.ttcsn5.webstudyenglish.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ttcsn5.webstudyenglish.entity.DictationTopics;

import java.util.List;

@Repository
public interface DictationTopicRepo extends JpaRepository<DictationTopics, Integer> {
    List<DictationTopics> findByCategory_Id(Integer categoryId);
    List<DictationTopics> findByCategory_IdAndTitleContainingIgnoreCase(Integer categoryId, String title);
}
