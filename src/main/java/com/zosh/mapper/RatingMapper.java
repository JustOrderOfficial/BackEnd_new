package com.zosh.mapper;

import com.zosh.dto.RatingDTO;
import com.zosh.modal.Product;
import com.zosh.modal.Rating;
import com.zosh.modal.User;

public class RatingMapper {

    public static RatingDTO toDTO(Rating rating) {
        if (rating == null) return null;

        return new RatingDTO(
            rating.getId(),
            rating.getUser().getId(),
            rating.getUser().getFirstName() + " " + rating.getUser().getLastName(),
            rating.getProduct().getId(),
            rating.getProduct().getTitle(),
            rating.getRating(),
            rating.getCreatedAt()
        );
    }

    // Overloaded method for general use
    public static Rating toEntity(RatingDTO dto) {
        if (dto == null) return null;

        Rating rating = new Rating();
        rating.setId(dto.getId());
        rating.setRating(dto.getRating());
        rating.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : java.time.LocalDateTime.now());
        return rating;
    }

    // Full conversion method (if needed elsewhere)
    public static Rating toEntity(RatingDTO dto, User user, Product product) {
        Rating rating = toEntity(dto);
        rating.setUser(user);
        rating.setProduct(product);
        return rating;
    }
}
