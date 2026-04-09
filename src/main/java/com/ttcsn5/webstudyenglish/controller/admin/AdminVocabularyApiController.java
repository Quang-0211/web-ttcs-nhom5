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
import com.ttcsn5.webstudyenglish.entity.Vocabulary;
import com.ttcsn5.webstudyenglish.entity.User;
import com.ttcsn5.webstudyenglish.repository.AccountRepo;
import com.ttcsn5.webstudyenglish.repository.CategoryRepo;
import com.ttcsn5.webstudyenglish.repository.VocabularyRepo;
import com.ttcsn5.webstudyenglish.service.MinioService;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/admin/api/vocabulary")
public class AdminVocabularyApiController {

    @Autowired
    private VocabularyRepo vocabularyRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private MinioService minioService;

    @PostMapping
    public ResponseEntity<Vocabulary> create(
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam("word") String word,
            @RequestParam("pronunciation") String pronunciation,
            @RequestParam("partOfSpeech") String partOfSpeech,
            @RequestParam("meaningVi") String meaningVi,
            @RequestParam("definitionEn") String definitionEn,
            @RequestParam("exampleVi") String exampleVi,
            @RequestParam("exampleEn") String exampleEn,
            @RequestParam("categoryId") Integer categoryId,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "audio", required = false) MultipartFile audio,
            HttpSession session) {

        Category category = categoryRepo.findById(categoryId).orElseThrow();
        
        Long userId = (Long) session.getAttribute("userId");
        User currentUser = null;
        if (userId != null) {
            currentUser = accountRepo.findById(userId).orElse(null);
        }
        
        Vocabulary vocab;
        if (id != null) {
            vocab = vocabularyRepo.findById(id).orElseThrow();
            if (currentUser != null) vocab.setUpdatedBy(currentUser);
        } else {
            vocab = new Vocabulary();
            if (currentUser != null) {
                vocab.setCreatedBy(currentUser);
                vocab.setUpdatedBy(currentUser);
            }
        }
        
        vocab.setWord(word);
        vocab.setPronunciation(pronunciation);
        vocab.setPartOfSpeech(partOfSpeech);
        vocab.setMeaningVi(meaningVi);
        vocab.setDefinitionEn(definitionEn);
        vocab.setExampleVi(exampleVi);
        vocab.setExampleEn(exampleEn);
        vocab.setCategory(category);

        if (image != null && !image.isEmpty()) {
            String imageUrl = minioService.uploadFile(image, "images");
            vocab.setImage(imageUrl);
        }
        if (audio != null && !audio.isEmpty()) {
            String audioUrl = minioService.uploadFile(audio, "audio");
            vocab.setAudio(audioUrl);
        }

        return ResponseEntity.ok(vocabularyRepo.save(vocab));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        vocabularyRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
