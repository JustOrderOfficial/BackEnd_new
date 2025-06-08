package com.zosh.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.zosh.dto.ProductDTO;
import com.zosh.dto.ProductDetailDTO;
import com.zosh.exception.ProductException;
import com.zosh.modal.Product;
import com.zosh.request.CreateProductRequest;

public interface ProductService {

    // only for admin
    public ProductDTO createProduct(CreateProductRequest req) throws ProductException;

    public String deleteProduct(Long productId) throws ProductException;

    public ProductDTO updateProduct(Long productId, Product product) throws ProductException;

    public List<ProductDTO> getAllProducts();

    // for user and admin both
    public Product findProductById(Long id) throws ProductException;

    public List<ProductDTO> findProductByCategory(String category);

    public List<ProductDTO> searchProduct(String query);

    public Page<ProductDTO> getAllProduct(
        String category,
        List<String> colors,
        List<String> sizes,
        Integer minPrice,
        Integer maxPrice,
        Integer minDiscount,
        String sort,
        String stock,
        Integer pageNumber,
        Integer pageSize
    );
    
    ProductDetailDTO findProductDetailsById(Long productId) throws ProductException;


    public List<ProductDTO> recentlyAddedProduct();

    public List<ProductDTO> getProductsByCategoryId(Long categoryId);
}
