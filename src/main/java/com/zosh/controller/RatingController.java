package com.zosh.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zosh.dto.UserDTO;
import com.zosh.exception.ProductException;
import com.zosh.exception.UserException;
import com.zosh.modal.Rating;
import com.zosh.request.RatingRequest;
import com.zosh.service.RatingServices;
import com.zosh.service.UserService;

@CrossOrigin(origins = "http://localhost:5176", allowCredentials = "true")
@RestController
@RequestMapping("/api/ratings")
public class RatingController {
    
    private final UserService userService;
    private final RatingServices ratingServices;
    
    public RatingController(UserService userService, RatingServices ratingServices) {
        this.ratingServices = ratingServices;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<Rating> createRatingHandler(@RequestBody RatingRequest req) throws UserException, ProductException {
        // Fetch user profile from SecurityContext
        UserDTO user = userService.findUserProfileByAuth();
        Rating rating = ratingServices.createRating(req, user);
        return new ResponseEntity<>(rating, HttpStatus.CREATED);
    }
    
    @GetMapping("/product/{productId}/ratings")
    public ResponseEntity<List<Rating>> getProductsReviewHandler(@PathVariable Long productId) {
        List<Rating> ratings = ratingServices.getProductsRating(productId);
        return new ResponseEntity<>(ratings, HttpStatus.OK);
    }
}
