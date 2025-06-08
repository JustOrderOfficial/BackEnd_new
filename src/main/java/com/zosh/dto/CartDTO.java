package com.zosh.dto;

import java.util.List;

public class CartDTO {

    private Long id;
    private Long userId;
    private int totalItems;
    private int totalPrice;
    private int totalDiscountedPrice;
    private int discountAmount;

    // ✅ New field to hold cart items
    private List<CartItemDTO> cartItems;

    public CartDTO() {
    }

    public CartDTO(Long id, Long userId, int totalItems, int totalPrice, int totalDiscountedPrice, int discountAmount, List<CartItemDTO> cartItems) {
        this.id = id;
        this.userId = userId;
        this.totalItems = totalItems;
        this.totalPrice = totalPrice;
        this.totalDiscountedPrice = totalDiscountedPrice;
        this.discountAmount = discountAmount;
        this.cartItems = cartItems;
    }

    // ✅ Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalDiscountedPrice() {
        return totalDiscountedPrice;
    }

    public void setTotalDiscountedPrice(int totalDiscountedPrice) {
        this.totalDiscountedPrice = totalDiscountedPrice;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }

    public List<CartItemDTO> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemDTO> cartItems) {
        this.cartItems = cartItems;
    }
}
