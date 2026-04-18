package com.ttcsn5.webstudyenglish.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ttcsn5.webstudyenglish.dto.ArticleDetailResponseDto;
import com.ttcsn5.webstudyenglish.dto.ArticlesUserHomeDto;
import com.ttcsn5.webstudyenglish.entity.Article;

@Repository
public interface ArticleRepo extends JpaRepository<Article, Integer> {

        Slice<Article> findByTitleContainingAndCategory_IdAndStatus(String title, int cateId, boolean status,
                        Pageable pageable);

        Slice<Article> findByTitleContainingAndStatus(String title, boolean status,
                        Pageable pageable);

        @Query("""
                                select new com.ttcsn5.webstudyenglish.dto.ArticlesUserHomeDto(a.id, a.title, a.image, c.name, a.createdAt)
                                from Article a
                                join a.category c
                                where (:title = '' or lower(a.title) like lower(concat('%', :title, '%')))
                                and (:categorySearch=0 or c.id = :categorySearch)

                        """)
        Page<ArticlesUserHomeDto> findArticleUserHome(Pageable pageable,
                        @Param("title") String title,
                        @Param("categorySearch") Integer categorySearch);

        @Query("""
                                select new com.ttcsn5.webstudyenglish.dto.ArticleDetailResponseDto(a.id, a.title, a.content, a.image, c.name, a.createdAt)
                                from Article a
                                join a.category c
                                where a.id=:id
                        """)
        ArticleDetailResponseDto findArticleDetail(Integer id);
}
