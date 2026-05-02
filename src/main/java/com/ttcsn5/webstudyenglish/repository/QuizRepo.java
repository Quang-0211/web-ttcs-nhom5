package com.ttcsn5.webstudyenglish.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ttcsn5.webstudyenglish.entity.Category;
import com.ttcsn5.webstudyenglish.entity.Quiz;

@Repository
public interface QuizRepo extends JpaRepository<Quiz, Integer> {
    List<Quiz> findByCategory(Category category);

    @Query("""
                    select a
                    from Quiz a
                    join a.category c
                    where (:title = '' or lower(a.title) like lower(concat('%', :title, '%')))
                    and (:categorySearch=0 or c.id = :categorySearch)
                    and c in :categories
            """)
    Page<Quiz> findQuizUserHomeAndCategoryPlan(Pageable pageable,
            @Param("title") String title,
            @Param("categorySearch") Integer categorySearch, @Param("categories") Set<Category> categories);
}