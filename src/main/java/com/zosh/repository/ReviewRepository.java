package com.zosh.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zosh.modal.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	@Query("SELECT r FROM Review r WHERE r.product.id = :productId")
	public List<Review> getAllProductsReview(@Param("productId") Long productId);
	
	
	@Query("SELECT r FROM Review r JOIN FETCH r.user WHERE r.product.id = :productId")
	List<Review> findByProductIdWithUser(@Param("productId") Long productId);


}
