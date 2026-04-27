package com.ttcsn5.webstudyenglish.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ttcsn5.webstudyenglish.entity.Video;
import com.ttcsn5.webstudyenglish.service.CourseService;
import com.ttcsn5.webstudyenglish.service.UploadImageAudio;
import com.ttcsn5.webstudyenglish.service.VideoService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminVideoController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UploadImageAudio uploadService;

    @GetMapping("/admin/videos")
    public String videos(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            Model model, HttpSession session) {
        Integer roleId = (Integer) session.getAttribute("roleId");
        if (roleId == null || roleId != 1) {
            return "redirect:/login";
        }
        List<Video> videos = videoService.search(keyword);
        model.addAttribute("keyword", keyword);
        model.addAttribute("videos", videos);
        model.addAttribute("courses", courseService.findPublished());
        model.addAttribute("path", "admin/video");
        return "admin/adminhome";
    }

    @PostMapping("/admin/videos")
    public String save(@RequestParam(name = "videoId", required = false, defaultValue = "") String videoId,
            @RequestParam("title") String title,
            @RequestParam("url") String url,
            @RequestParam(name = "thumbnail", required = false) MultipartFile thumbnail,
            @RequestParam(name = "duration", required = false, defaultValue = "0") Integer duration,
            @RequestParam(name = "subtitle", required = false, defaultValue = "") String subtitle,
            @RequestParam(name = "description", required = false, defaultValue = "") String description,
            @RequestParam(name = "status", required = false, defaultValue = "true") Boolean status,
            @RequestParam(name = "courseId", required = false, defaultValue = "0") Integer courseId) throws Exception {

        String thumbnailPath = uploadService.upload(thumbnail, "images");
        Video video = videoId.isBlank() ? new Video() : videoService.findById(Integer.parseInt(videoId));
        if (video == null) {
            video = new Video();
        }

        video.setTitle(title);
        video.setUrl(url);
        video.setDuration(duration);
        video.setSubtitle(subtitle);
        video.setDescription(description);
        video.setStatus(status);
        video.setCourse(courseId != null && courseId > 0 ? courseService.findById(courseId) : null);
        if (thumbnailPath != null) {
            video.setThumbnail(thumbnailPath);
        }

        videoService.save(video);
        return "redirect:/admin/videos";
    }

    @PostMapping("/admin/videos/delete")
    public String delete(@RequestParam("videoId") Integer videoId) {
        videoService.delete(videoId);
        return "redirect:/admin/videos";
    }
}