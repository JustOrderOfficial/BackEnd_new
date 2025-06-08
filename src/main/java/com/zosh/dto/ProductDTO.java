package com.zosh.dto;

public class ProductDTO {
    private Long id;
    private String title;
    private String imageUrl;
    private int price;
    private int discountedPrice;
    private int discountPersent;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public int getDiscountedPrice() { return discountedPrice; }
    public void setDiscountedPrice(int discountedPrice) { this.discountedPrice = discountedPrice; }

    public int getDiscountPersent() { return discountPersent; }
    public void setDiscountPersent(int discountPersent) { this.discountPersent = discountPersent; }
}
