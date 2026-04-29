package com.ttcsn5.webstudyenglish.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ttcsn5.webstudyenglish.entity.Video;

@Repository
public interface VideoRepo extends JpaRepository<Video, Integer> {

    List<Video> findByStatusTrueOrderByCreatedAtDesc();

    List<Video> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(String title);

    List<Video> findByCategory_IdAndStatusTrueOrderByCreatedAtDesc(Integer cateId);
}