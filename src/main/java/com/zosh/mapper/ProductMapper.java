package com.zosh.mapper;

import com.zosh.dto.ProductDTO;
import com.zosh.modal.Product;

public class ProductMapper {

    public static ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setImageUrl(product.getImageUrl());
        dto.setPrice(product.getPrice());
        dto.setDiscountedPrice(product.getDiscountedPrice());
        dto.setDiscountPersent(product.getDiscountPersent());
        return dto;
    }

    // Optional if you need to map back
    public static Product toEntity(ProductDTO dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setTitle(dto.getTitle());
        product.setImageUrl(dto.getImageUrl());
        product.setPrice(dto.getPrice());
        product.setDiscountedPrice(dto.getDiscountedPrice());
        product.setDiscountPersent(dto.getDiscountPersent());
        return product;
    }
}
