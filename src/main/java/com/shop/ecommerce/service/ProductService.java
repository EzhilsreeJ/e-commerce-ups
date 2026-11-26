package com.shop.ecommerce.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.shop.ecommerce.exception.ResourceNotFoundException;
import com.shop.ecommerce.model.Category;
import com.shop.ecommerce.model.Product;
import com.shop.ecommerce.repository.CategoryRepository;
import com.shop.ecommerce.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
    }

    public Product createProduct(Product product) {
        attachCategory(product);
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product details) {
        Product existing = getProduct(id);
        existing.setName(details.getName());
        existing.setDescription(details.getDescription());
        existing.setPrice(details.getPrice());
        existing.setStock(details.getStock());
        existing.setImageUrl(details.getImageUrl());
        existing.setCategory(details.getCategory());
        attachCategory(existing);
        return productRepository.save(existing);
    }

    public void deleteProduct(Long id) {
        productRepository.delete(getProduct(id));
    }

    private void attachCategory(Product product) {
        if (product.getCategory() != null && product.getCategory().getId() != null) {
            Long categoryId = product.getCategory().getId();
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + categoryId));
            product.setCategory(category);
        }
    }
}
