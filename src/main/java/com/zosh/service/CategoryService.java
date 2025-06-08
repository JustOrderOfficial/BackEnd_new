package com.zosh.service;

import java.util.List;

import com.zosh.dto.CategoryDTO;

public interface CategoryService {

    CategoryDTO getCategoryByName(String name);
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);
    void deleteCategory(Long id);
    List<CategoryDTO> getCategories(String name);
    List<CategoryDTO> getAllCategories();
    List<CategoryDTO> getCategoriesByParentId(Long parentId);

}
