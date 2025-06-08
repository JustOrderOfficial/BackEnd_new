package com.zosh.mapper;

import java.util.stream.Collectors;

import com.zosh.dto.CartDTO;
import com.zosh.modal.Cart;

public class CartMapper {

    // Entity to DTO
    public static CartDTO toCartDTO(Cart cart) {
        return new CartDTO(
                cart.getId(),
                cart.getUser().getId(),
                cart.getTotalItem(),
                (int) cart.getTotalPrice(),
                cart.getTotalDiscountedPrice(),
                cart.getDiscounte(),
                cart.getCartItems().stream()
                .map(CartItemMapper::toCartItemDTO)
                    .collect(Collectors.toList())
        );
    }

    // DTO to Entity (unchanged)
    public static Cart toCartEntity(CartDTO cartDTO) {
        Cart cart = new Cart();
        cart.setId(cartDTO.getId());
        cart.setTotalItem(cartDTO.getTotalItems());
        cart.setTotalPrice(cartDTO.getTotalPrice());
        cart.setTotalDiscountedPrice(cartDTO.getTotalDiscountedPrice());
        cart.setDiscounte(cartDTO.getDiscountAmount());
        return cart;
    }
}
