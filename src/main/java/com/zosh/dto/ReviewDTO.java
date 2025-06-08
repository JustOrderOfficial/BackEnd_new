package com.zosh.dto;

import java.time.LocalDateTime;

public class ReviewDTO {

    private Long id;
    private String review;

    private Long productId;
    private String productName; // Optional - remove if not needed

    private Long userId;
    private String userName; // Optional - remove if not needed

    private LocalDateTime createdAt;

    public ReviewDTO() {
    }

    public ReviewDTO(Long id, String review, Long productId, String productName, Long userId, String userName, LocalDateTime createdAt) {
        this.id = id;
        this.review = review;
        this.productId = productId;
        this.productName = productName;
        this.userId = userId;
        this.userName = userName;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
