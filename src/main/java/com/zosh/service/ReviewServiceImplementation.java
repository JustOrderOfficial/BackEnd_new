package com.zosh.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.zosh.dto.ProductDTO;
import com.zosh.dto.ReviewDTO;
import com.zosh.dto.UserDTO;
import com.zosh.exception.ProductException;
import com.zosh.mapper.ReviewMapper;
import com.zosh.mapper.UserMapper;  // Make sure this mapper exists to handle conversion
import com.zosh.modal.Product;
import com.zosh.modal.Review;
import com.zosh.modal.User;
import com.zosh.repository.ProductRepository;
import com.zosh.repository.ReviewRepository;
import com.zosh.repository.UserRepository;
import com.zosh.request.ReviewRequest;

@Service
public class ReviewServiceImplementation implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ReviewServiceImplementation(ReviewRepository reviewRepository, ProductService productService, ProductRepository productRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.productService = productService;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Review createReview(ReviewRequest req, UserDTO userDTO) throws ProductException {
        // ✅ Fetch the actual User entity from the DB
        User user = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userDTO.getId()));

        // ✅ Fetch the actual Product entity from the DB
        Product product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new ProductException("Product not found with ID: " + req.getProductId()));

        // ✅ Create and populate Review
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setReview(req.getReview());
        review.setCreatedAt(LocalDateTime.now());

        // ✅ Save and return
        return reviewRepository.save(review);
    }


    @Override
    public List<Review> getAllReview(Long productId) {
        return reviewRepository.getAllProductsReview(productId);
    }
    
    
    @Override
    public List<ReviewDTO> getReviewsByProductId(Long productId) {
        // Use the JOIN FETCH query to eagerly load User data
        List<Review> reviews = reviewRepository.findByProductIdWithUser(productId);

        // Map each Review entity to ReviewDTO (which includes username)
        return reviews.stream()
                .map(ReviewMapper::toDTO)
                .collect(Collectors.toList());
    }
}
