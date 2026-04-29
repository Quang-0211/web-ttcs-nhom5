package com.ttcsn5.webstudyenglish.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ttcsn5.webstudyenglish.entity.Course;
import com.ttcsn5.webstudyenglish.service.CourseService;
import com.ttcsn5.webstudyenglish.service.UploadImageAudio;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminCourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UploadImageAudio uploadService;

    @GetMapping("/admin/courses")
    public String courses(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            Model model, HttpSession session) {
        Integer roleId = (Integer) session.getAttribute("roleId");
        if (roleId == null || roleId != 1) {
            return "redirect:/login";
        }
        List<Course> courses = courseService.search(keyword);
        model.addAttribute("keyword", keyword);
        model.addAttribute("courses", courses);
        model.addAttribute("path", "admin/course");
        return "admin/adminhome";
    }

    @PostMapping("/admin/courses")
    public String save(@RequestParam(name = "courseId", required = false, defaultValue = "") String courseId,
            @RequestParam("title") String title,
            @RequestParam(name = "description", required = false, defaultValue = "") String description,
            @RequestParam(name = "level", required = false, defaultValue = "Beginner") String level,
            @RequestParam(name = "durationHours", required = false, defaultValue = "0") Integer durationHours,
            @RequestParam(name = "status", required = false, defaultValue = "true") Boolean status,
            @RequestParam(name = "thumbnail", required = false) MultipartFile thumbnail) throws Exception {

        String thumbnailPath = uploadService.upload(thumbnail, "images");
        Course course = courseId.isBlank() ? new Course() : courseService.findById(Integer.parseInt(courseId));
        if (course == null) {
            course = new Course();
        }

        course.setTitle(title);
        course.setDescription(description);
        course.setLevel(level);
        course.setDurationHours(durationHours);
        course.setStatus(status);
        if (thumbnailPath != null) {
            course.setThumbnail(thumbnailPath);
        }

        courseService.save(course);
        return "redirect:/admin/courses";
    }

    @PostMapping("/admin/courses/delete")
    public String delete(@RequestParam("courseId") Integer courseId) {
        courseService.delete(courseId);
        return "redirect:/admin/courses";
    }
}