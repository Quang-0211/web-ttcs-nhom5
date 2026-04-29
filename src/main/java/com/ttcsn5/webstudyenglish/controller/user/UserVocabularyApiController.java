package com.ttcsn5.webstudyenglish.controller.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ttcsn5.webstudyenglish.service.UserVocabularyService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/notebook")
public class UserVocabularyApiController {

    @Autowired
    private UserVocabularyService userVocabularyService;

    @PostMapping("/save/{vocabId}")
    public ResponseEntity<Map<String, Object>> saveVocabulary(@PathVariable("vocabId") Integer vocabId, HttpSession session) {
        System.out.println("====== HIT SAVE API for vocabId: " + vocabId + " ======");
        Map<String, Object> response = new HashMap<>();
        
        Object userIdObj = session.getAttribute("userId");
        if (userIdObj == null) {
            response.put("success", false);
            response.put("message", "User not logged in");
            return ResponseEntity.status(401).body(response);
        }

        Long userId = Long.valueOf(userIdObj.toString());
        boolean saved = userVocabularyService.saveVocabulary(userId, vocabId);
        
        if (saved) {
            response.put("success", true);
            response.put("message", "Vocabulary saved successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Vocabulary already saved or not found");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/unsave/{vocabId}")
    public ResponseEntity<Map<String, Object>> unsaveVocabulary(@PathVariable("vocabId") Integer vocabId, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        Object userIdObj = session.getAttribute("userId");
        if (userIdObj == null) {
            response.put("success", false);
            response.put("message", "User not logged in");
            return ResponseEntity.status(401).body(response);
        }

        Long userId = Long.valueOf(userIdObj.toString());
        boolean removed = userVocabularyService.unsaveVocabulary(userId, vocabId);
        
        if (removed) {
            response.put("success", true);
            response.put("message", "Vocabulary removed successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Vocabulary not found in notebook");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
