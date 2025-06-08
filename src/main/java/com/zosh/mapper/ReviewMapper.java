package com.zosh.mapper;

import com.zosh.dto.ReviewDTO;
import com.zosh.modal.Product;
import com.zosh.modal.Review;
import com.zosh.modal.User;

public class ReviewMapper {

    public static ReviewDTO toDTO(Review review) {
        if (review == null) return null;

        return new ReviewDTO(
            review.getId(),
            review.getReview(),
            review.getProduct().getId(),
            review.getProduct().getTitle(),
            review.getUser().getId(),
            review.getUser().getFirstName() + " " + review.getUser().getLastName(),
            review.getCreatedAt()
        );
    }

    // ✅ Simplified version for stream mapping
    public static Review toEntity(ReviewDTO dto) {
        if (dto == null) return null;

        Review review = new Review();
        review.setId(dto.getId());
        review.setReview(dto.getReview());
        review.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : java.time.LocalDateTime.now());
        return review;
    }

    // ✅ Full version when User and Product are available
    public static Review toEntity(ReviewDTO dto, User user, Product product) {
        Review review = toEntity(dto);
        if (review == null || user == null || product == null) return null;

        review.setUser(user);
        review.setProduct(product);
        return review;
    }
}
