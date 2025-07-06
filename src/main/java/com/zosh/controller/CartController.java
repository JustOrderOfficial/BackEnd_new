package com.zosh.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zosh.dto.CartDTO;
import com.zosh.dto.CartItemDTO;
import com.zosh.dto.UserDTO;
import com.zosh.exception.CartException;
import com.zosh.exception.CartItemException;
import com.zosh.exception.ProductException;
import com.zosh.exception.UserException;
import com.zosh.request.AddItemRequest;
import com.zosh.response.ApiResponse;
import com.zosh.service.CartItemService;
import com.zosh.service.CartService;
import com.zosh.service.UserService;

@CrossOrigin(origins = "http://localhost:5176", allowCredentials = "true")
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final UserService userService;

    public CartController(CartService cartService, CartItemService cartItemService, UserService userService) {
        this.cartService = cartService;
        this.cartItemService = cartItemService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<CartDTO> findUserCartHandler() throws UserException, CartException {
        // Fetch user profile from SecurityContext
        UserDTO user = userService.findUserProfileByAuth();
        CartDTO cartDTO = cartService.findUserCart(user.getId());
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @PutMapping("/add")
    public ResponseEntity<CartItemDTO> addItemToCart(@RequestBody AddItemRequest req) throws UserException, ProductException, CartItemException, CartException {
        // Fetch user profile from SecurityContext
        UserDTO user = userService.findUserProfileByAuth();
        CartItemDTO cartItemDTO = cartItemService.addCartItem(user.getId(), req);
        return new ResponseEntity<>(cartItemDTO, HttpStatus.ACCEPTED);
    }
    
    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse> clearUserCart() throws UserException, CartException {
        UserDTO user = userService.findUserProfileByAuth();
        cartService.clearCart(user.getId());
        return new ResponseEntity<>(new ApiResponse("Cart cleared successfully", true), HttpStatus.OK);
    }
    
    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<CartItemDTO> updateCartItemQuantity(
            @PathVariable Long cartItemId,
            @RequestBody Map<String, Integer> requestBody
    ) throws UserException, CartItemException {
        int quantity = requestBody.get("quantity");
        UserDTO user = userService.findUserProfileByAuth();
        CartItemDTO updated = cartItemService.updateQuantityForUserCartItem(user.getId(), cartItemId, quantity);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable Long cartItemId) 
            throws UserException, CartItemException {
        
        UserDTO user = userService.findUserProfileByAuth();
        cartItemService.removeCartItem(user.getId(), cartItemId);

        return new ResponseEntity<>(new ApiResponse("Item deleted successfully", true), HttpStatus.OK);
    }





}
