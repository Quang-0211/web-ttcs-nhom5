package com.ttcsn5.webstudyenglish.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ttcsn5.webstudyenglish.entity.Course;

@Repository
public interface CourseRepo extends JpaRepository<Course, Integer> {

    List<Course> findByStatusTrueOrderByCreatedAtDesc();

    List<Course> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(String title);
}