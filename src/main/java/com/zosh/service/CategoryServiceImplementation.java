package com.zosh.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zosh.dto.CategoryDTO;
import com.zosh.mapper.CategoryMapper;
import com.zosh.modal.Category;
import com.zosh.repository.CategoryRepository;

@Service
public class CategoryServiceImplementation implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImplementation(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Admin: Create a new category
    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = CategoryMapper.toEntity(categoryDTO);
        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.toDTO(savedCategory);
    }

    // Admin: Update an existing category
    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category existingCategory = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        existingCategory.setName(categoryDTO.getName());
        existingCategory.setLevel(categoryDTO.getLevel());
        if (categoryDTO.getParentCategoryId() != null) {
            existingCategory.setParentCategory(new Category(categoryDTO.getParentCategoryId()));
        }
        Category updatedCategory = categoryRepository.save(existingCategory);
        return CategoryMapper.toDTO(updatedCategory);
    }

    // Admin: Delete a category
    @Override
    public void deleteCategory(Long id) {
        Category existingCategory = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        categoryRepository.delete(existingCategory);
    }

    // Admin or User: Get categories with filtering support
    @Override
    public List<CategoryDTO> getCategories(String name) {
        List<Category> categories;

        if (name != null && !name.isEmpty()) {
            categories = categoryRepository.findByNameContainingIgnoreCase(name);
        } else {
            categories = categoryRepository.findAll();
        }

        return categories.stream()
                         .map(CategoryMapper::toDTO)
                         .collect(Collectors.toList());
    }


    @Override
    public CategoryDTO getCategoryByName(String name) {
        // Fetch category by name from the repository
        Category category = categoryRepository.findByName(name);
        
        // Check if the category is found
        if (category != null) {
            // If found, map it to CategoryDTO and return it
            return CategoryMapper.toDTO(category);
        }
        
        // If no category is found, throw an exception or handle it based on your needs
        throw new RuntimeException("Category not found with name: " + name);
    }
    
    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> new CategoryDTO(category.getId(), category.getName()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<CategoryDTO> getCategoriesByParentId(Long parentId) {
        List<Category> categories;

        if (parentId == null) {
            // Get top-level categories
            categories = categoryRepository.findByParentCategoryIdIsNull();
        } else {
            categories = categoryRepository.findByParentCategoryId(parentId);
        }

        return categories.stream()
            .map(cat -> new CategoryDTO(
                cat.getId(),
                cat.getName(),
                cat.getLevel(),
                cat.getParentCategory() != null ? cat.getParentCategory().getId() : null,
                		cat.getImageUrl()
            ))
            .collect(Collectors.toList());
    }



}
