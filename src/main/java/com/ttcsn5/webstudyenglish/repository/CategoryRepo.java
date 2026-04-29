package com.ttcsn5.webstudyenglish.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ttcsn5.webstudyenglish.entity.Category;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Integer> {
    @Query("select c from Category c where c.parent.name = :name")
    List<Category> findByParent(String name);

    Category findAllById(int i);

    Category findByName(String cateName);

    List<Category> findByParentNameIgnoreCase(String parentName);

    Category findAllByName(String string);

    // @Query("select c from Category c where c.parent.name = :name and c.id in
    // :ids")
    // Set<Category> findAllByIdInAndParentName(Set<Integer> ids, String name);
    Set<Category> findByIdInAndParent_Name(Set<Integer> ids, String name);

    
}
