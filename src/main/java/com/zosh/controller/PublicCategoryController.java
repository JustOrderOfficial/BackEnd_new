package com.zosh.controller;

import com.zosh.dto.CategoryDTO;
import com.zosh.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5176", allowCredentials = "true")
@RestController
@RequestMapping("/api/categories")
public class PublicCategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("all")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/public/parent/{parentId}")
    public ResponseEntity<List<CategoryDTO>> getCategoriesByParent(@PathVariable Long parentId) {
        return ResponseEntity.ok(categoryService.getCategoriesByParentId(parentId));
    }

    @GetMapping("/public/top")
    public ResponseEntity<List<CategoryDTO>> getTopCategories() {
        return ResponseEntity.ok(categoryService.getCategoriesByParentId(null));
    }

}
