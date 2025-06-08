package com.zosh.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zosh.dto.ProductDTO;
import com.zosh.exception.ProductException;
import com.zosh.mapper.ProductDetailMapper;
import com.zosh.modal.Product;
import com.zosh.request.CreateProductRequest;
import com.zosh.response.ApiResponse;
import com.zosh.service.ProductService;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/")
    public ResponseEntity<ProductDTO> createProductHandler(@RequestBody CreateProductRequest req) throws ProductException {
        ProductDTO createdProduct = productService.createProduct(req);
        return new ResponseEntity<>(createdProduct, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProductHandler(@PathVariable Long productId) throws ProductException {
        String msg = productService.deleteProduct(productId);
        ApiResponse res = new ApiResponse(msg, true);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDTO>> findAllProduct() {
        List<ProductDTO> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<ProductDTO>> recentlyAddedProduct() {
        List<ProductDTO> products = productService.recentlyAddedProduct();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PutMapping("/{productId}/update")
    public ResponseEntity<ProductDTO> updateProductHandler(
            @RequestBody CreateProductRequest req,
            @PathVariable Long productId) throws ProductException {

        Product product = ProductDetailMapper.fromCreateRequest(req);
        ProductDTO updatedProduct = productService.updateProduct(productId, product);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }



    @PostMapping("/creates")
    public ResponseEntity<ApiResponse> createMultipleProduct(@RequestBody CreateProductRequest[] reqs) throws ProductException {
        for (CreateProductRequest product : reqs) {
            productService.createProduct(product);
        }
        ApiResponse res = new ApiResponse("Products created successfully", true);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }
}
