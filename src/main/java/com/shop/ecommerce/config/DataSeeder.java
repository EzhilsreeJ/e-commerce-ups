package com.shop.ecommerce.config;

import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.shop.ecommerce.model.Category;
import com.shop.ecommerce.model.Product;
import com.shop.ecommerce.repository.CategoryRepository;
import com.shop.ecommerce.repository.ProductRepository;

@Component
public class DataSeeder implements CommandLineRunner {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public DataSeeder(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (categoryRepository.count() > 0) {
            return;
        }

        Category electronics = categoryRepository.save(new Category("Electronics", "Phones, laptops and gadgets"));
        Category books = categoryRepository.save(new Category("Books", "Programming and fiction books"));
        Category clothing = categoryRepository.save(new Category("Clothing", "T-shirts, jackets and shoes"));

        productRepository.save(newProduct("Laptop 14 inch", "Lightweight laptop with 16 GB RAM", "899.99", 10, "laptop", electronics));
        productRepository.save(newProduct("Smartphone", "6.5 inch display, 128 GB storage", "499.50", 25, "phone", electronics));
        productRepository.save(newProduct("Wireless Headphones", "Noise cancelling, 30 hour battery", "129.99", 40, "headphones", electronics));
        productRepository.save(newProduct("Java for Beginners", "Learn Java step by step", "29.99", 50, "javabook", books));
        productRepository.save(newProduct("Spring Boot in Practice", "Build REST APIs with Spring", "39.99", 30, "springbook", books));
        productRepository.save(newProduct("Cotton T-Shirt", "Soft 100 percent cotton", "14.99", 100, "tshirt", clothing));
    }

    private Product newProduct(String name, String description, String price, int stock,
                               String imageKey, Category category) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(new BigDecimal(price));
        product.setStock(stock);
        product.setImageUrl("https://picsum.photos/seed/" + imageKey + "/400/300");
        product.setCategory(category);
        return product;
    }
}
