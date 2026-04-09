package com.ttcsn5.webstudyenglish.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ttcsn5.webstudyenglish.entity.DictationTopics;

@Repository
public interface DictationTopicRepo extends JpaRepository<DictationTopics, Integer> {
}
