package com.zosh.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zosh.dto.CartDTO;
import com.zosh.dto.CartItemDTO;
import com.zosh.dto.UserDTO;
import com.zosh.exception.CartException;
import com.zosh.exception.CartItemException;
import com.zosh.exception.UserException;
import com.zosh.mapper.CartItemMapper;
import com.zosh.mapper.CartMapper;
import com.zosh.modal.Cart;
import com.zosh.modal.CartItem;
import com.zosh.modal.Product;
import com.zosh.repository.CartItemRepository;
import com.zosh.repository.CartRepository;
import com.zosh.repository.ProductRepository;
import com.zosh.repository.UserRepository;
import com.zosh.request.AddItemRequest;

import jakarta.transaction.Transactional;

@Service
public class CartItemServiceImplementation implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartService cartService;  // Inject CartService
    private final ProductRepository productRepository;  // Inject ProductRepository
    private final UserService userService;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    @Autowired
    public CartItemServiceImplementation(CartItemRepository cartItemRepository, CartService cartService, 
                                         ProductRepository productRepository, UserService userService,UserRepository userRepository,CartRepository cartRepository) {
        this.cartItemRepository = cartItemRepository;
        this.cartService = cartService;
        this.productRepository = productRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public CartItemDTO createCartItem(CartItemDTO cartItemDTO) {
        CartItem cartItem = CartItemMapper.toCartItemEntity(cartItemDTO);
        cartItem.setQuantity(1);
        cartItem.setPrice(cartItem.getProduct().getPrice());
        cartItem.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice());

        return CartItemMapper.toCartItemDTO(cartItemRepository.save(cartItem));
    }

    @Override
    public CartItemDTO updateCartItem(Long userId, Long id, CartItemDTO cartItemDTO) throws CartItemException, UserException {
        CartItem item = findCartItem(id);
        UserDTO user = userService.findUserById(item.getUserId());

        if (user.getId().equals(userId)) {
            item.setQuantity(cartItemDTO.getQuantity());
            item.setPrice(item.getQuantity() * item.getProduct().getPrice());
            item.setDiscountedPrice(item.getQuantity() * item.getProduct().getDiscountedPrice());
            return CartItemMapper.toCartItemDTO(cartItemRepository.save(item));
        } else {
            throw new CartItemException("You can't update another user's cart item");
        }
    }

    @Override
    public CartItemDTO isCartItemExist(Cart cart, Product product, String size, Long userId) {
        CartItem item = cartItemRepository.isCartItemExist(cart, product, size, userId);
        return item != null ? CartItemMapper.toCartItemDTO(item) : null;
    }

    @Override
    @Transactional
    public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException {
        System.out.println("🧨 removeCartItem called with userId = " + userId + ", cartItemId = " + cartItemId);

        CartItem cartItem = findCartItem(cartItemId);
        System.out.println("🧾 Found cart item with ID: " + cartItem.getId() + ", userId = " + cartItem.getUserId());

        if (!cartItem.getUserId().equals(userId)) {
            throw new UserException("You can't remove another user's item");
        }

        Cart cart = cartItem.getCart();

        // ✅ Remove cart item from the cart's set
        cart.getCartItems().remove(cartItem);

        // ✅ Now remove from repository
        cartItemRepository.deleteById(cartItemId);
        System.out.println("✅ Deleted cart item from repository and removed from cart");

        // 🔁 Recalculate cart totals
        int totalItems = cart.getCartItems().stream().mapToInt(CartItem::getQuantity).sum();
        double totalPrice = cart.getCartItems().stream().mapToDouble(CartItem::getPrice).sum();

        cart.setTotalItem(totalItems);
        cart.setTotalPrice((int) totalPrice);
        cart.setTotalDiscountedPrice((int) totalPrice); 
        cart.setDiscounte(0);

        cartRepository.save(cart);
        System.out.println("🧾 Updated cart totals and saved");
    }





    @Override
    public CartItemDTO findCartItemById(Long cartItemId) throws CartItemException {
        return CartItemMapper.toCartItemDTO(findCartItem(cartItemId));
    }

    private CartItem findCartItem(Long cartItemId) throws CartItemException {
        return cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemException("CartItem not found with id: " + cartItemId));
    }

    @Override
    public CartItemDTO addCartItem(Long userId, AddItemRequest request) 
            throws CartItemException, UserException, CartException {

        CartDTO cartDTO = cartService.findUserCart(userId); 
        Cart cart = CartMapper.toCartEntity(cartDTO);

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new CartItemException("Product not found"));

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setSize(request.getSize());
        cartItem.setQuantity(request.getQuantity());
        
        cartItem.setPrice(product.getPrice() * request.getQuantity());
        cartItem.setDiscountedPrice(product.getDiscountedPrice() * request.getQuantity());

        cartItem.setUserId(userId); // ✅ Fixed: set userId as Long

        CartItem savedCartItem = cartItemRepository.save(cartItem);
        return CartItemMapper.toCartItemDTO(savedCartItem);
    }
    
    @Override
    public CartItemDTO updateQuantityForUserCartItem(Long userId, Long cartItemId, int quantity)
            throws CartItemException, UserException {

        // Get the cart item
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemException("Cart item not found with ID: " + cartItemId));

        // Verify ownership
        if (!cartItem.getCart().getUser().getId().equals(userId)) {
            throw new CartItemException("You are not authorized to update this cart item");
        }

        // Update quantity and price
        cartItem.setQuantity(quantity);
        cartItem.setPrice(cartItem.getProduct().getDiscountedPrice() * quantity);

        CartItem updatedCartItem = cartItemRepository.save(cartItem);

        // Update parent cart
        Cart cart = cartItem.getCart();
        Set<CartItem> cartItems = cart.getCartItems();

        int totalItems = cartItems.stream().mapToInt(CartItem::getQuantity).sum();
        int totalPrice = (int) cartItems.stream().mapToDouble(CartItem::getPrice).sum();

        cart.setTotalItem(totalItems);
        cart.setTotalPrice(totalPrice);
        cart.setTotalDiscountedPrice(totalPrice); // adjust if needed
        cart.setDiscounte(0); // adjust if needed

        cartRepository.save(cart);

        return CartItemMapper.toCartItemDTO(updatedCartItem);
    }




}
