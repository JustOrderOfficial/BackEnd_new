package com.zosh.mapper;

import com.zosh.dto.CartItemDTO;
import com.zosh.modal.CartItem;
import com.zosh.modal.Product;

public class CartItemMapper {

    // Entity to DTO
    public static CartItemDTO toCartItemDTO(CartItem cartItem) {
        return new CartItemDTO(
                cartItem.getId(),
                cartItem.getProduct(),  // passing full Product object
                cartItem.getSize(),
                cartItem.getQuantity(),
                cartItem.getPrice(),
                cartItem.getDiscountedPrice(),
                cartItem.getUserId()
        );
    }

    // DTO to Entity
    public static CartItem toCartItemEntity(CartItemDTO cartItemDTO) {
        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemDTO.getId());
        cartItem.setProduct(cartItemDTO.getProduct());  // setting full Product
        cartItem.setSize(cartItemDTO.getSize());
        cartItem.setQuantity(cartItemDTO.getQuantity());
        cartItem.setPrice(cartItemDTO.getPrice());
        cartItem.setDiscountedPrice(cartItemDTO.getDiscountedPrice());
        cartItem.setUserId(cartItemDTO.getUserId());
        return cartItem;
    }
}
