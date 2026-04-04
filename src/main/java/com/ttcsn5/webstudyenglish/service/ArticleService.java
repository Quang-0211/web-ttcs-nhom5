package com.ttcsn5.webstudyenglish.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ttcsn5.webstudyenglish.entity.Article;
import com.ttcsn5.webstudyenglish.repository.ArticleRepo;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepo articleRepo;

    public List<Article> searchArticle(String title, int cateId, Boolean status, int cnt) {
        Pageable pageable = PageRequest.of(cnt, 10, Sort.by("id").ascending());
        if (cateId == 0) {
            return articleRepo
                    .findByTitleContainingAndStatus(title, status, pageable)
                    .getContent();
        }
        return articleRepo
                .findByTitleContainingAndCategory_IdAndStatus(title, cateId, status, pageable)
                .getContent();
    }

    public Article save(Article article) {
        return articleRepo.save(article);
    }

    public void updateStatus(int id, boolean status) {
        Article art = articleRepo.findById(id).get();
        art.setStatus(status);
        articleRepo.save(art);
    }
}
