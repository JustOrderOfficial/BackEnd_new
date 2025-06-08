package com.zosh.repository;

import com.zosh.modal.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Find category by exact name
    Category findByName(String name);

    // Find category by name and parent category name
    @Query("SELECT c FROM Category c WHERE c.name = :name AND c.parentCategory.name = :parentCategoryName")
    Category findByNameAndParent(@Param("name") String name, @Param("parentCategoryName") String parentCategoryName);

    // Find categories whose name contains a keyword (case-insensitive)
    List<Category> findByNameContainingIgnoreCase(String name);

    // Get all categories
    List<Category> findAll();

	List<Category> findByParentCategoryIdIsNull();

	List<Category> findByParentCategoryId(Long parentId);
    
    
}
