package com.zosh.service;

import java.util.List;

import com.zosh.dto.ReviewDTO;
import com.zosh.dto.UserDTO;
import com.zosh.exception.ProductException;
import com.zosh.modal.Review;
import com.zosh.request.ReviewRequest;

public interface ReviewService {

	public Review createReview(ReviewRequest req,UserDTO user) throws ProductException;
	
	public List<Review> getAllReview(Long productId);
	
    List<ReviewDTO> getReviewsByProductId(Long productId);

	
	
}
