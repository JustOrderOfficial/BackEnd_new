package com.zosh.service;

import java.util.List;

import com.zosh.dto.UserDTO;
import com.zosh.exception.ProductException;
import com.zosh.modal.Rating;
import com.zosh.modal.User;
import com.zosh.request.RatingRequest;

public interface RatingServices {
	
	public Rating createRating(RatingRequest req,UserDTO user) throws ProductException;
	
	public List<Rating> getProductsRating(Long productId);

}
