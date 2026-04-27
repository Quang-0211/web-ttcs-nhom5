package com.ttcsn5.webstudyenglish.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ttcsn5.webstudyenglish.entity.Category;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Integer> {

    List<Category> findByParent(Category cate);

    Category findAllById(int i);

    Category findByName(String cateName);

    List<Category> findByParentNameIgnoreCase(String parentName);

    Category findAllByName(String string);
}
