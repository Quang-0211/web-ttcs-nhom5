package com.ttcsn5.webstudyenglish.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ttcsn5.webstudyenglish.entity.Category;
import com.ttcsn5.webstudyenglish.entity.DictationSentences;
import com.ttcsn5.webstudyenglish.entity.DictationTopics;
import com.ttcsn5.webstudyenglish.entity.User;
import com.ttcsn5.webstudyenglish.repository.AccountRepo;
import com.ttcsn5.webstudyenglish.repository.CategoryRepo;
import com.ttcsn5.webstudyenglish.repository.DictationSentenceRepo;
import com.ttcsn5.webstudyenglish.repository.DictationTopicRepo;
import com.ttcsn5.webstudyenglish.service.MinioService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/admin/api/dictation")
public class AdminDictationApiController {

    @Autowired
    private DictationTopicRepo topicRepo;

    @Autowired
    private DictationSentenceRepo sentenceRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private MinioService minioService;

    @PostMapping
    public ResponseEntity<DictationTopics> createOrUpdate(
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("categoryId") Integer categoryId,
            @RequestParam("sentences") List<String> sentences,
            @RequestParam("audioUrls") List<String> audioUrls,
            @RequestParam(value = "audios", required = false) List<MultipartFile> audios,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).build();
        User currentUser = accountRepo.findById(userId).orElse(null);
        if (currentUser == null) return ResponseEntity.status(401).build();

        Category category = categoryRepo.findById(categoryId).orElseThrow();

        DictationTopics topic;
        if (id != null) {
            topic = topicRepo.findById(id).orElseThrow();
            topic.setUpdatedBy(currentUser);
        } else {
            topic = new DictationTopics();
            topic.setCreatedBy(currentUser);
            topic.setUpdatedBy(currentUser);
        }

        topic.setTitle(title);
        topic.setDescription(description);
        topic.setCategory(category);
        topic = topicRepo.save(topic);

        if (id != null) {
            List<DictationSentences> oldSentences = sentenceRepo.findByTopic(topic);
            sentenceRepo.deleteAll(oldSentences);
        }

        for (int i = 0; i < sentences.size(); i++) {
            DictationSentences ds = new DictationSentences();
            ds.setTopic(topic);
            ds.setTitle(sentences.get(i));
            ds.setCorrectIndex(i);
            
            // Re-use old URL if preserved during edit, otherwise upload new
            String finalUrl = audioUrls.size() > i ? audioUrls.get(i) : null;
            
            if (audios != null && audios.size() > i) {
                MultipartFile audio = audios.get(i);
                // 10 bytes check is to ignore empty blobs from JS
                if (audio != null && !audio.isEmpty() && audio.getSize() > 10) {
                    finalUrl = minioService.uploadFile(audio, "audio");
                }
            }
            ds.setAudioUrl(finalUrl);
            sentenceRepo.save(ds);
        }

        return ResponseEntity.ok(topic);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        DictationTopics topic = topicRepo.findById(id).orElseThrow();
        List<DictationSentences> sents = sentenceRepo.findByTopic(topic);
        sentenceRepo.deleteAll(sents);
        topicRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/sentences")
    public ResponseEntity<List<DictationSentences>> getSentences(@PathVariable("id") Integer id) {
        DictationTopics topic = topicRepo.findById(id).orElseThrow();
        return ResponseEntity.ok(sentenceRepo.findByTopic(topic));
    }
}
