package com.ttcsn5.webstudyenglish.dto.response;

import java.time.LocalDateTime;

public class ArticleDetailResponse {
    private Integer id;
    private String title;
    private String content;
    private String image;
    private String cateName;
    private int cateId;
    private LocalDateTime createdAt;

    public ArticleDetailResponse(Integer id, String title, String content, String image, String cateName,
            LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.image = image;
        this.cateName = cateName;
        this.createdAt = createdAt;
    }

    public ArticleDetailResponse(Integer id, String title, String content, String image, String cateName, int cateId,
            LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.image = image;
        this.cateName = cateName;
        this.cateId = cateId;
        this.createdAt = createdAt;
    }

    public String getTitle() {
        return title;
    }

    public int getCateId() {
        return cateId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
