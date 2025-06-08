
package com.zosh.mapper;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.zosh.dto.ProductDetailDTO;
import com.zosh.modal.Product;
import com.zosh.modal.Rating;
import com.zosh.modal.Review;
import com.zosh.modal.Size;
import com.zosh.request.CreateProductRequest;

public class ProductDetailMapper {

    public static ProductDetailDTO toDetailDTO(Product product) {
        ProductDetailDTO dto = new ProductDetailDTO();
        dto.setId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setDiscountedPrice(product.getDiscountedPrice());
        dto.setDiscountPersent(product.getDiscountPersent());
        dto.setQuantity(product.getQuantity());
        dto.setBrand(product.getBrand());
        dto.setColor(product.getColor());
        dto.setImageUrl(product.getImageUrl());
        dto.setNumRatings(product.getNumRatings());
        dto.setCreatedAt(product.getCreatedAt());

        if (product.getCategory() != null) {
            dto.setCategory(CategoryMapper.toDTO(product.getCategory()));
        }

        if (product.getSizes() != null) {
            dto.setSizes(product.getSizes().stream()
                    .map(SizeMapper::toDTO)
                    .collect(Collectors.toList()));
        }

        if (product.getRatings() != null) {
            dto.setRatings(product.getRatings().stream()
                    .map(RatingMapper::toDTO)
                    .collect(Collectors.toList()));
        }

        if (product.getReviews() != null) {
            dto.setReviews(product.getReviews().stream()
                    .map(ReviewMapper::toDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public static Product toEntity(ProductDetailDTO dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setTitle(dto.getTitle());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setDiscountedPrice(dto.getDiscountedPrice());
        product.setDiscountPersent(dto.getDiscountPersent());
        product.setQuantity(dto.getQuantity());
        product.setBrand(dto.getBrand());
        product.setColor(dto.getColor());
        product.setImageUrl(dto.getImageUrl());
        product.setNumRatings(dto.getNumRatings());
        product.setCreatedAt(dto.getCreatedAt());

        if (dto.getCategory() != null) {
            product.setCategory(CategoryMapper.toEntity(dto.getCategory()));
        }

        if (dto.getSizes() != null) {
            Set<Size> sizes = dto.getSizes().stream()
                    .map(SizeMapper::toEntity)
                    .peek(size -> size.setProduct(product))
                    .collect(Collectors.toSet());
            product.setSizes(sizes);
        }

        if (dto.getRatings() != null) {
        	List<Rating> ratings = dto.getRatings().stream()
        		    .map(RatingMapper::toEntity)
        		    .peek(rating -> rating.setProduct(product))
        		    .collect(Collectors.toList());
        		product.setRatings(ratings); // ✅ Works fine

        }

        if (dto.getReviews() != null) {
            List<Review> reviews = dto.getReviews().stream()
                    .map(ReviewMapper::toEntity)
                    .peek(review -> review.setProduct(product))
                    .collect(Collectors.toList());
            product.setReviews(reviews);
        }

        return product;
    }
    
    

        // ... existing toDetailDTO and toEntity methods

        public static Product fromCreateRequest(CreateProductRequest req) {
            Product product = new Product();
            product.setTitle(req.getTitle());
            product.setDescription(req.getDescription());
            product.setPrice(req.getPrice());
            product.setDiscountedPrice(req.getDiscountedPrice());
            product.setDiscountPersent(req.getDiscountPersent());
            product.setQuantity(req.getQuantity());
            product.setBrand(req.getBrand());
            product.setColor(req.getColor());
            product.setImageUrl(req.getImageUrl());

            if (req.getSize() != null) {
                Set<Size> sizes = req.getSize().stream()
                        .map(SizeMapper::toEntity)
                        .peek(size -> size.setProduct(product))
                        .collect(Collectors.toSet());
                product.setSizes(sizes);
            }

            // Note: Category setting can be handled in service using categoryService.findByName()

            return product;
        }


}
