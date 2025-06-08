package com.zosh.service;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.zosh.modal.Cart;
import com.zosh.modal.User;
import com.zosh.repository.CartRepository;
import com.zosh.repository.UserRepository;
import com.zosh.utill.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    // Register user with all fields
    @Autowired
    private CartRepository cartRepository;

    public String registerUser(String email, String firstName, String lastName, String mobile, String password, String role) {
        if (userRepository.findByEmail(email).isPresent()) {
            return "Email already exists";
        }

        User user = new User();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setMobile(mobile);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setCreatedAt(LocalDateTime.now());

        // Save user first
        User savedUser = userRepository.save(user);

        // ✅ Create empty cart for the user
        Cart cart = new Cart();
        cart.setUser(savedUser);
        cart.setTotalItem(0);
        cart.setTotalPrice(0);
        cart.setDiscounte(0);
        cart.setTotalDiscountedPrice(0);
        cartRepository.save(cart);  // Save the empty cart

        return "User registered successfully";
    }


    // Login user with email and password
    public String loginUser(String email, String password) {
        try {
            // Authenticate the user using AuthenticationManager
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
            );
        } catch (BadCredentialsException e) {
            return "Invalid credentials";  // Handle invalid login attempts
        }

        // Load the user details from the repository
        UserDetails userDetails = userRepository.findByEmail(email)
                .map(u -> new org.springframework.security.core.userdetails.User(u.getEmail(), u.getPassword(), new ArrayList<>()))
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate and return the JWT token
        return jwtUtil.generateToken(userDetails);
    }
}
