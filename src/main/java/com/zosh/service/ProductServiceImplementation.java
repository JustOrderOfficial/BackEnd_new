package com.zosh.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.zosh.dto.CategoryDTO;
import com.zosh.dto.ProductDTO;
import com.zosh.dto.ProductDetailDTO;
import com.zosh.dto.RatingDTO;
import com.zosh.dto.ReviewDTO;
import com.zosh.dto.SizeDTO;
import com.zosh.exception.ProductException;
import com.zosh.mapper.ProductMapper;
import com.zosh.mapper.SizeMapper;
import com.zosh.modal.Category;
import com.zosh.modal.Product;
import com.zosh.modal.Size;
import com.zosh.repository.CategoryRepository;
import com.zosh.repository.ProductRepository;
import com.zosh.request.CreateProductRequest;

@Service
public class ProductServiceImplementation implements ProductService {

    private final ProductRepository productRepository;
    private final UserService userService;
    private final CategoryRepository categoryRepository;

    public ProductServiceImplementation(ProductRepository productRepository, UserService userService, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ProductDTO createProduct(CreateProductRequest req) {
        // 1. Top-level Category
        Category topLevel = categoryRepository.findByName(req.getTopLavelCategory());
        if (topLevel == null) {
            Category topLevelCategory = new Category();
            topLevelCategory.setName(req.getTopLavelCategory());
            topLevelCategory.setLevel(1);
            topLevel = categoryRepository.save(topLevelCategory);
        }

        // 2. Second-level Category
        Category secondLevel = categoryRepository.findByNameAndParent(req.getSecondLavelCategory(), topLevel.getName());
        if (secondLevel == null) {
            Category secondLevelCategory = new Category();
            secondLevelCategory.setName(req.getSecondLavelCategory());
            secondLevelCategory.setParentCategory(topLevel);
            secondLevelCategory.setLevel(2);
            secondLevel = categoryRepository.save(secondLevelCategory);
        }

        // 3. Third-level Category
        Category thirdLevel = categoryRepository.findByNameAndParent(req.getThirdLavelCategory(), secondLevel.getName());
        if (thirdLevel == null) {
            Category thirdLevelCategory = new Category();
            thirdLevelCategory.setName(req.getThirdLavelCategory());
            thirdLevelCategory.setParentCategory(secondLevel);
            thirdLevelCategory.setLevel(3);
            thirdLevel = categoryRepository.save(thirdLevelCategory);
        }

        // 4. Create Product
        Product product = new Product();
        product.setTitle(req.getTitle());
        product.setColor(req.getColor());
        product.setDescription(req.getDescription());
        product.setDiscountedPrice(req.getDiscountedPrice());
        product.setDiscountPersent(req.getDiscountPersent());
        product.setImageUrl(req.getImageUrl());
        product.setBrand(req.getBrand());
        product.setPrice(req.getPrice());
        product.setQuantity(req.getQuantity());
        product.setCategory(thirdLevel);
        product.setCreatedAt(LocalDateTime.now());

        // 5. Convert SizeDTO → Size Entity and Set in Product
        if (req.getSize() != null) {
            Set<Size> sizes = req.getSize().stream()
                .map(SizeMapper::toEntity)
                .peek(size -> size.setProduct(product)) // optional reverse link
                .collect(Collectors.toSet());
            product.setSizes(sizes);
        }

        // 6. Save and Return DTO
        Product savedProduct = productRepository.save(product);
        return ProductMapper.toDTO(savedProduct);
    }

    @Override
    public String deleteProduct(Long productId) throws ProductException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException("Product not found with ID: " + productId));

        // Clear associations to avoid constraint violations
        if (product.getSizes() != null) {
            product.getSizes().clear();
        }

        if (product.getRatings() != null) {
            product.getRatings().clear();
        }

        if (product.getReviews() != null) {
            product.getReviews().clear();
        }

        productRepository.delete(product);
        return "Product deleted successfully";
    }


    @Override
    public ProductDTO updateProduct(Long productId, Product productReq) throws ProductException {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException("Product not found with ID: " + productId));

        // Only update fields if present in the request
        if (productReq.getQuantity() != 0) {
            existingProduct.setQuantity(productReq.getQuantity());
        }
        if (productReq.getDescription() != null) {
            existingProduct.setDescription(productReq.getDescription());
        }
        if (productReq.getTitle() != null) {
            existingProduct.setTitle(productReq.getTitle());
        }
        if (productReq.getPrice() != 0) {
            existingProduct.setPrice(productReq.getPrice());
        }
        if (productReq.getDiscountPersent() != 0) {
            existingProduct.setDiscountPersent(productReq.getDiscountPersent());
        }
        if (productReq.getDiscountedPrice() != 0) {
            existingProduct.setDiscountedPrice(productReq.getDiscountedPrice());
        }
        if (productReq.getBrand() != null) {
            existingProduct.setBrand(productReq.getBrand());
        }
        if (productReq.getColor() != null) {
            existingProduct.setColor(productReq.getColor());
        }
        if (productReq.getImageUrl() != null) {
            existingProduct.setImageUrl(productReq.getImageUrl());
        }

        Product updatedProduct = productRepository.save(existingProduct);
        return ProductMapper.toDTO(updatedProduct);
    }


    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Product findProductById(Long id) throws ProductException {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductException("Product not found with id " + id));
    }



    @Override
    public List<ProductDTO> findProductByCategory(String category) {
        List<Product> products = productRepository.findByCategory(category);
        return products.stream().map(ProductMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> searchProduct(String query) {
        List<Product> products = productRepository.searchProduct(query);
        return products.stream().map(ProductMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public Page<ProductDTO> getAllProduct(String category, List<String> colors, List<String> sizes, Integer minPrice, Integer maxPrice,
                                          Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Product> products = productRepository.filterProducts(category, minPrice, maxPrice, minDiscount, sort);

        if (!colors.isEmpty()) {
            products = products.stream()
                    .filter(p -> colors.stream().anyMatch(c -> c.equalsIgnoreCase(p.getColor())))
                    .collect(Collectors.toList());
        }

        if (stock != null) {
            if (stock.equals("in_stock")) {
                products = products.stream().filter(p -> p.getQuantity() > 0).collect(Collectors.toList());
            } else if (stock.equals("out_of_stock")) {
                products = products.stream().filter(p -> p.getQuantity() < 1).collect(Collectors.toList());
            }
        }

        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), products.size());
        List<ProductDTO> pageContent = products.subList(startIndex, endIndex).stream()
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(pageContent, pageable, products.size());
    }

    @Override
    public List<ProductDTO> recentlyAddedProduct() {
        List<Product> products = productRepository.findTop10ByOrderByCreatedAtDesc();
        return products.stream().map(ProductMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getProductsByCategoryId(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return products.stream().map(ProductMapper::toDTO).collect(Collectors.toList());
    }
    
    
    @Override
    public ProductDetailDTO findProductDetailsById(Long productId) throws ProductException {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductException("Product not found with ID: " + productId));

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

        // Category
        if (product.getCategory() != null) {
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setId(product.getCategory().getId());
            categoryDTO.setName(product.getCategory().getName());
            dto.setCategory(categoryDTO);
        }

        // Sizes
        List<SizeDTO> sizeDTOs = product.getSizes().stream().map(size -> {
            SizeDTO sizeDTO = new SizeDTO();
            sizeDTO.setId(size.getId());
            sizeDTO.setName(size.getName());
            return sizeDTO;
        }).collect(Collectors.toList());
        dto.setSizes(sizeDTOs);

        // Ratings
     // Ratings
        List<RatingDTO> ratingDTOs = product.getRatings().stream().map(rating -> {
            RatingDTO ratingDTO = new RatingDTO();
            ratingDTO.setId(rating.getId());
            ratingDTO.setUserId(rating.getUser() != null ? rating.getUser().getId() : null);
            ratingDTO.setUserName(rating.getUser() != null ? rating.getUser().getFirstName() + " " + rating.getUser().getLastName() : null);
            ratingDTO.setProductId(product.getId());
            ratingDTO.setProductName(product.getTitle());
            ratingDTO.setRating(rating.getRating());
            ratingDTO.setCreatedAt(rating.getCreatedAt());
            return ratingDTO;
        }).collect(Collectors.toList());

        dto.setRatings(ratingDTOs);


        // Reviews
     // Reviews
        List<ReviewDTO> reviewDTOs = product.getReviews().stream().map(review -> {
            ReviewDTO rev = new ReviewDTO();
            rev.setId(review.getId());
            rev.setReview(review.getReview()); // updated: getReview() not getContent()
            rev.setUserId(review.getUser() != null ? review.getUser().getId() : null);
            rev.setCreatedAt(review.getCreatedAt());
            return rev;
        }).collect(Collectors.toList());
        dto.setReviews(reviewDTOs);


        return dto;
    }
}
