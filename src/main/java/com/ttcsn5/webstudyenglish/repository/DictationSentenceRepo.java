package com.ttcsn5.webstudyenglish.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ttcsn5.webstudyenglish.entity.DictationSentences;
import com.ttcsn5.webstudyenglish.entity.DictationTopics;

@Repository
public interface DictationSentenceRepo extends JpaRepository<DictationSentences, Integer> {
    List<DictationSentences> findByTopic(DictationTopics topic);
    void deleteByTopic(DictationTopics topic);
}
