package com.zosh.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zosh.dto.CartItemDTO;
import com.zosh.dto.UserDTO;
import com.zosh.exception.CartItemException;
import com.zosh.exception.UserException;
import com.zosh.mapper.CartItemMapper;
import com.zosh.modal.CartItem;
import com.zosh.response.ApiResponse;
import com.zosh.service.CartItemService;
import com.zosh.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "http://localhost:5176", allowCredentials = "true")

@RestController
@RequestMapping("/api/cart_items")
@Tag(name="Cart Item Management", description = "Create, update, and delete cart items")
public class CartItemController {

    private final CartItemService cartItemService;
    private final UserService userService;

    public CartItemController(CartItemService cartItemService, UserService userService) {
        this.cartItemService = cartItemService;
        this.userService = userService;
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItemHandler(@PathVariable Long cartItemId) throws CartItemException, UserException {
        // Fetch user profile from SecurityContext
        UserDTO userDTO = userService.findUserProfileByAuth();
        // Remove cart item using service layer
        cartItemService.removeCartItem(userDTO.getId(), cartItemId);
        ApiResponse response = new ApiResponse("Item removed from cart", true);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartItemDTO> updateCartItemHandler(@PathVariable Long cartItemId, @RequestBody CartItemDTO cartItemDTO) throws CartItemException, UserException {
        // Fetch user profile from SecurityContext
        UserDTO userDTO = userService.findUserProfileByAuth();
        // Update cart item using service
        CartItemDTO updatedCartItemDTO = cartItemService.updateCartItem(userDTO.getId(), cartItemId, cartItemDTO);
        return new ResponseEntity<>(updatedCartItemDTO, HttpStatus.ACCEPTED);
    }
}
