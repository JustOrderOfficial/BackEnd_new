package com.zosh.mapper;

import com.zosh.dto.CategoryDTO;
import com.zosh.modal.Category;

public class CategoryMapper {

    // Convert Entity to DTO
    public static CategoryDTO toDTO(Category category) {
        if (category == null) {
            return null;
        }
        return new CategoryDTO(
            category.getId(),
            category.getName(),
            category.getLevel(),
            category.getParentCategory() != null ? category.getParentCategory().getId() : null,
            category.getImageUrl()
        );
    }

    // Convert DTO to Entity
    public static Category toEntity(CategoryDTO categoryDTO) {
        if (categoryDTO == null) {
            return null;
        }
        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        category.setLevel(categoryDTO.getLevel());
        // Handle parent category if necessary, you might want to fetch it or pass it
        category.setImageUrl(categoryDTO.getImageUrl());
        return category;
    }
}
