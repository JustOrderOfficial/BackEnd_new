package com.zosh.service;

import com.zosh.dto.CartItemDTO;
import com.zosh.exception.CartException;
import com.zosh.exception.CartItemException;
import com.zosh.exception.UserException;
import com.zosh.modal.Cart;
import com.zosh.modal.Product;
import com.zosh.request.AddItemRequest;

public interface CartItemService {

    CartItemDTO createCartItem(CartItemDTO cartItemDTO);

    CartItemDTO updateCartItem(Long userId, Long id, CartItemDTO cartItemDTO) throws CartItemException, UserException;

    CartItemDTO isCartItemExist(Cart cart, Product product, String size, Long userId);

    void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException;

    CartItemDTO findCartItemById(Long cartItemId) throws CartItemException;
    
    CartItemDTO addCartItem(Long userId, AddItemRequest request) throws CartItemException, UserException, CartException;  // Add this method

    CartItemDTO updateQuantityForUserCartItem(Long userId, Long cartItemId, int quantity) throws CartItemException, UserException;

}
