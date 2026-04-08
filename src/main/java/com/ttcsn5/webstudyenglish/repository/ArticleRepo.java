package com.ttcsn5.webstudyenglish.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ttcsn5.webstudyenglish.entity.Article;

@Repository
public interface ArticleRepo extends JpaRepository<Article, Integer> {

    Slice<Article> findByTitleContainingAndCategory_IdAndStatus(String title, int cateId, boolean status,
            Pageable pageable);

    Slice<Article> findByTitleContainingAndStatus(String title, boolean status,
            Pageable pageable);
}
