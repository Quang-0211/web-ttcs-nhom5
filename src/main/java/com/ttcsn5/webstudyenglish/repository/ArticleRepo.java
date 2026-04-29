package com.ttcsn5.webstudyenglish.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ttcsn5.webstudyenglish.dto.response.ArticleDetailResponse;
import com.ttcsn5.webstudyenglish.dto.response.ArticlesUserHomeResponse;
import com.ttcsn5.webstudyenglish.entity.Article;
import com.ttcsn5.webstudyenglish.entity.Category;

@Repository
public interface ArticleRepo extends JpaRepository<Article, Integer> {

        Slice<Article> findByTitleContainingAndCategory_IdAndStatus(String title, int cateId, boolean status,
                        Pageable pageable);

        Slice<Article> findByTitleContainingAndStatus(String title, boolean status,
                        Pageable pageable);

        @Query("""
                                select new com.ttcsn5.webstudyenglish.dto.response.ArticlesUserHomeResponse(a.id, a.title, a.image, c.name, c.id, a.createdAt)
                                from Article a
                                join a.category c
                                where (:title = '' or lower(a.title) like lower(concat('%', :title, '%')))
                                and (:categorySearch=0 or c.id = :categorySearch)

                        """)
        Page<ArticlesUserHomeResponse> findArticleUserHome(Pageable pageable,
                        @Param("title") String title,
                        @Param("categorySearch") Integer categorySearch);

        @Query("""
                                select new com.ttcsn5.webstudyenglish.dto.response.ArticleDetailResponse(a.id, a.title, a.content, a.image, c.name, c.id, a.createdAt)
                                from Article a
                                join a.category c
                                where a.id=:id
                        """)
        ArticleDetailResponse findArticleDetail(Integer id);

        @Query("select count(a.id) from Article a")
        int countArticle();

        @Query("""
                                select new com.ttcsn5.webstudyenglish.dto.response.ArticlesUserHomeResponse(a.id, a.title, a.image, c.name, c.id, a.createdAt)
                                from Article a
                                join a.category c
                                where (:title = '' or lower(a.title) like lower(concat('%', :title, '%')))
                                and (:categorySearch=0 or c.id = :categorySearch)
                                and c in :categories
                        """)
        Page<ArticlesUserHomeResponse> findArticleUserHomeAndCategoryPlan(Pageable pageable,
                        @Param("title") String title,
                        @Param("categorySearch") Integer categorySearch, @Param("categories") Set<Category> categories);
}
