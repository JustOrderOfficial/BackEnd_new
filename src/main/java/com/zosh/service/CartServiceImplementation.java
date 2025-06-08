package com.zosh.service;

import org.springframework.stereotype.Service;

import com.zosh.dto.CartDTO;
import com.zosh.dto.UserDTO;
import com.zosh.exception.CartException;
import com.zosh.exception.UserException;
import com.zosh.mapper.CartMapper;
import com.zosh.mapper.UserMapper;
import com.zosh.modal.Cart;
import com.zosh.modal.User;
import com.zosh.repository.CartItemRepository;
import com.zosh.repository.CartRepository;

@Service
public class CartServiceImplementation implements CartService {

    private final CartRepository cartRepository;
    private final UserService userService;
    private final CartItemRepository cartItemRepository;

    public CartServiceImplementation(CartRepository cartRepository, UserService userService, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.userService = userService;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public CartDTO createCart(UserDTO userDTO) throws UserException {
        User user = UserMapper.toEntity(userDTO); // Convert UserDTO to Entity

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalItem(0);
        cart.setTotalPrice(0);
        cart.setTotalDiscountedPrice(0);
        cart.setDiscounte(0);

        return CartMapper.toCartDTO(cartRepository.save(cart));
    }


    @Override
    public CartDTO getCartByUserId(Long userId) throws CartException {
        Cart cart = cartRepository.findByUserId(userId)
                                  .orElseThrow(() -> new CartException("Cart not found for user id: " + userId)); // Unwrap Optional
        return CartMapper.toCartDTO(cart); // Map to CartDTO
    }


    @Override
    public void clearCart(Long userId) throws UserException, CartException {
        Cart cart = cartRepository.findByUserId(userId)
                                  .orElseThrow(() -> new CartException("Cart not found for user with ID: " + userId)); // Unwrap Optional
        cartItemRepository.deleteByCartIdAndUserId(cart.getId(), userId);
        cart.setTotalItem(0);
        cart.setTotalPrice(0);
        cart.setTotalDiscountedPrice(0);
        cart.setDiscounte(0);
        cartRepository.save(cart);
    }


    @Override
    public CartDTO updateCart(Long userId, CartDTO cartDTO) throws CartException, UserException {
        Cart cart = cartRepository.findByUserId(userId)
                                  .orElseThrow(() -> new CartException("Cart not found for user id: " + userId)); // Unwrap Optional

        cart.setTotalItem(cartDTO.getTotalItems());
        cart.setTotalPrice(cartDTO.getTotalPrice());
        cart.setTotalDiscountedPrice(cartDTO.getTotalDiscountedPrice());
        cart.setDiscounte(cartDTO.getDiscountAmount());

        return CartMapper.toCartDTO(cartRepository.save(cart)); // Save and return the CartDTO
    }

    
    @Override
    public CartDTO findUserCart(Long userId) throws CartException {
        Cart cart = cartRepository.findByUserId(userId)
                   .orElseThrow(() -> new CartException("Cart not found for user with ID: " + userId)); // Unwrap Optional
        return CartMapper.toCartDTO(cart);  // Convert Cart to CartDTO
    }
    
    
    
    
 


}
