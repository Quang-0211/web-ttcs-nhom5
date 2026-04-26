package com.ttcsn5.webstudyenglish.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ttcsn5.webstudyenglish.entity.Course;
import com.ttcsn5.webstudyenglish.repository.CourseRepo;

@Service
public class CourseService {

    @Autowired
    private CourseRepo courseRepo;

    public List<Course> findAll() {
        return courseRepo.findAll();
    }

    public List<Course> findPublished() {
        return courseRepo.findByStatusTrueOrderByCreatedAtDesc();
    }

    public List<Course> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return courseRepo.findAll();
        }
        return courseRepo.findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(keyword.trim());
    }

    public Course findById(Integer id) {
        return courseRepo.findById(id).orElse(null);
    }

    public Course save(Course course) {
        return courseRepo.save(course);
    }

    public void delete(Integer id) {
        courseRepo.deleteById(id);
    }
}