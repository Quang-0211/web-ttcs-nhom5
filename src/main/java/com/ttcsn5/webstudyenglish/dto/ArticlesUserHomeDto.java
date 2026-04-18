package com.ttcsn5.webstudyenglish.dto;

import java.time.LocalDateTime;

public class ArticlesUserHomeDto {
    private Integer id;
    private String title;
    private String image;
    private String cateName;
    private LocalDateTime createdAt;
    

    public ArticlesUserHomeDto(Integer id, String title, String image, String cateName, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.cateName = cateName;
        this.createdAt = createdAt;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }

}
