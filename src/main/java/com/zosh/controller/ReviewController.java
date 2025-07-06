package com.zosh.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.zosh.dto.ReviewDTO;
import com.zosh.dto.UserDTO;
import com.zosh.exception.ProductException;
import com.zosh.exception.UserException;
import com.zosh.mapper.ReviewMapper;
import com.zosh.modal.Review;
import com.zosh.request.ReviewRequest;
import com.zosh.service.ProductService;
import com.zosh.service.ReviewService;
import com.zosh.service.UserService;

@CrossOrigin(origins = "http://localhost:5176", allowCredentials = "true")
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    
    private final ReviewService reviewService;
    private final UserService userService;
    private final ProductService productService;
    
    public ReviewController(ReviewService reviewService, UserService userService, ProductService productService) {
        this.reviewService = reviewService;
        this.userService = userService;
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<ReviewDTO> createReviewHandler(@RequestBody ReviewRequest req) throws UserException, ProductException {
        System.out.println("[ReviewController] Incoming request to create review for Product ID: " + req.getProductId());
        System.out.println("[ReviewController] Review Text: " + req.getReview());

        UserDTO user = userService.findUserProfileByAuth();
        System.out.println("[ReviewController] Authenticated user ID: " + user.getId());

        Review review = reviewService.createReview(req, user);
        ReviewDTO reviewDTO = ReviewMapper.toDTO(review);

        System.out.println("[ReviewController] Review created successfully. Review ID: " + review.getId());
        return new ResponseEntity<>(reviewDTO, HttpStatus.CREATED);
    }


    // ✅ Get all reviews for a product (recommended)
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewDTO>> getProductReviews(@PathVariable Long productId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByProductId(productId);
        return ResponseEntity.ok(reviews); // ✅ Already mapped, just return
    }


}
