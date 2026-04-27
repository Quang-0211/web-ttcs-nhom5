package com.ttcsn5.webstudyenglish.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ttcsn5.webstudyenglish.entity.Video;
import com.ttcsn5.webstudyenglish.repository.VideoRepo;

@Service
public class VideoService {

    @Autowired
    private VideoRepo videoRepo;

    public List<Video> findAll() {
        return videoRepo.findAll();
    }

    public List<Video> findPublished() {
        return videoRepo.findByStatusTrueOrderByCreatedAtDesc();
    }

    public List<Video> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return videoRepo.findAll();
        }
        return videoRepo.findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(keyword.trim());
    }

    public List<Video> findByCourse(Integer courseId) {
        return videoRepo.findByCourse_IdAndStatusTrueOrderByCreatedAtDesc(courseId);
    }

    public Video findById(Integer id) {
        return videoRepo.findById(id).orElse(null);
    }

    public Video save(Video video) {
        return videoRepo.save(video);
    }

    public void delete(Integer id) {
        videoRepo.deleteById(id);
    }
}