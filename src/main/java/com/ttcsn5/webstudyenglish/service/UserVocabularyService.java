package com.ttcsn5.webstudyenglish.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ttcsn5.webstudyenglish.entity.User;
import com.ttcsn5.webstudyenglish.entity.UserVocabulary;
import com.ttcsn5.webstudyenglish.entity.Vocabulary;
import com.ttcsn5.webstudyenglish.repository.AccountRepo;
import com.ttcsn5.webstudyenglish.repository.UserVocabularyRepo;
import com.ttcsn5.webstudyenglish.repository.VocabularyRepo;

import jakarta.transaction.Transactional;

@Service
public class UserVocabularyService {

    @Autowired
    private UserVocabularyRepo userVocabularyRepo;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private VocabularyRepo vocabularyRepo;

    @Transactional
    public boolean saveVocabulary(Long userId, Integer vocabId) {
        Optional<User> userOpt = accountRepo.findById(userId);
        Optional<Vocabulary> vocabOpt = vocabularyRepo.findById(vocabId);

        if (userOpt.isPresent() && vocabOpt.isPresent()) {
            User user = userOpt.get();
            Vocabulary vocab = vocabOpt.get();

            if (!userVocabularyRepo.existsByUserAndVocabulary(user, vocab)) {
                UserVocabulary uv = new UserVocabulary();
                uv.setUser(user);
                uv.setVocabulary(vocab);
                userVocabularyRepo.save(uv);
                return true;
            }
        }
        return false;
    }

    @Transactional
    public boolean unsaveVocabulary(Long userId, Integer vocabId) {
        Optional<User> userOpt = accountRepo.findById(userId);
        Optional<Vocabulary> vocabOpt = vocabularyRepo.findById(vocabId);

        if (userOpt.isPresent() && vocabOpt.isPresent()) {
            User user = userOpt.get();
            Vocabulary vocab = vocabOpt.get();

            if (userVocabularyRepo.existsByUserAndVocabulary(user, vocab)) {
                userVocabularyRepo.deleteByUserAndVocabulary(user, vocab);
                return true;
            }
        }
        return false;
    }

    public boolean isSaved(Long userId, Integer vocabId) {
        if (userId == null || vocabId == null) return false;
        
        Optional<User> userOpt = accountRepo.findById(userId);
        Optional<Vocabulary> vocabOpt = vocabularyRepo.findById(vocabId);

        if (userOpt.isPresent() && vocabOpt.isPresent()) {
            return userVocabularyRepo.existsByUserAndVocabulary(userOpt.get(), vocabOpt.get());
        }
        return false;
    }

    public List<UserVocabulary> getSavedVocabularies(Long userId) {
        if (userId == null) return List.of();
        
        Optional<User> userOpt = accountRepo.findById(userId);
        if (userOpt.isPresent()) {
            return userVocabularyRepo.findByUserOrderByIdDesc(userOpt.get());
        }
        return List.of();
    }
}
