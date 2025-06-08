package com.zosh.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zosh.dto.ProductDTO;
import com.zosh.dto.ProductDetailDTO;
import com.zosh.dto.ReviewDTO;
import com.zosh.exception.ProductException;
import com.zosh.mapper.ProductDetailMapper;
import com.zosh.modal.Product;
import com.zosh.service.ProductService;
import com.zosh.service.ReviewService;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api")
public class UserProductController {

    private ProductService productService;
    private ReviewService reviewService;

    public UserProductController(ProductService productService) {
    	this.reviewService = reviewService;
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<Page<ProductDTO>> findProductByCategoryHandler(
            @RequestParam String category,
            @RequestParam List<String> color,
            @RequestParam List<String> size,
            @RequestParam Integer minPrice,
            @RequestParam Integer maxPrice,
            @RequestParam Integer minDiscount,
            @RequestParam String sort,
            @RequestParam String stock,
            @RequestParam Integer pageNumber,
            @RequestParam Integer pageSize) {

        Page<ProductDTO> res = productService.getAllProduct(
                category, color, size, minPrice, maxPrice, minDiscount,
                sort, stock, pageNumber, pageSize);

        System.out.println("complete products");
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @GetMapping("/products/id/{productId}")
    public ResponseEntity<ProductDetailDTO> findProductByIdHandler(@PathVariable Long productId) throws ProductException {
        ProductDetailDTO product = productService.findProductDetailsById(productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<ProductDTO>> searchProductHandler(@RequestParam String q) {
        List<ProductDTO> products = productService.searchProduct(q);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/products/category/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductDTO> products = productService.getProductsByCategoryId(categoryId);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    
    
    @GetMapping("/id/{productId}")
    public ResponseEntity<ProductDetailDTO> getProductWithReviews(@PathVariable Long productId) throws ProductException {
        Product product = productService.findProductById(productId);
        ProductDetailDTO productDTO = ProductDetailMapper.toDetailDTO(product);

        List<ReviewDTO> reviews = reviewService.getReviewsByProductId(productId);
        productDTO.setReviews(reviews);

        return ResponseEntity.ok(productDTO);
    }

}
