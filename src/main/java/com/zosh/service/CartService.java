package com.zosh.service;

import com.zosh.dto.CartDTO;
import com.zosh.dto.UserDTO;
import com.zosh.exception.CartException;
import com.zosh.exception.UserException;

public interface CartService {

	CartDTO createCart(UserDTO user) throws UserException;

    CartDTO getCartByUserId(Long userId) throws CartException;

    void clearCart(Long userId) throws UserException, CartException;

    CartDTO updateCart(Long userId, CartDTO cartDTO) throws CartException, UserException;
    
    CartDTO findUserCart(Long userId) throws CartException;
}
