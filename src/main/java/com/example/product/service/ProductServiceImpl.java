package com.example.product.service;

import com.example.product.dto.ProductDto;
import com.example.product.entity.Category;
import com.example.product.entity.Product;
import com.example.product.entity.Product.Status;
import com.example.product.exception.ResouceNotFoundException;
import com.example.product.repo.CategoryRepo;
import com.example.product.repo.ProductRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceImpl {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    public Product createProduct(ProductDto dto) throws ResouceNotFoundException {
        Category category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResouceNotFoundException("Category not found"));

        Product product = new Product();
        product.setName(dto.getName());
        product.setSku(dto.getSkuCode());
        product.setDescription(dto.getDescription());
        product.setCategory(category);
        product.setPrice(BigDecimal.valueOf(dto.getPrice()));
        product.setStock(dto.getStockQuantity());
        product.setStatus(Status.valueOf(dto.getStatus().toUpperCase()));

        return productRepo.save(product);
    }

    public Product getProductById(Long id) throws ResouceNotFoundException {
        return productRepo.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Product not found with id " + id));
    }

    public Product updateProduct(Long id, ProductDto dto) throws ResouceNotFoundException {
        Product product = getProductById(id);

        Category category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResouceNotFoundException("Category not found"));

        product.setName(dto.getName());
        product.setSku(dto.getSkuCode());
        product.setDescription(dto.getDescription());
        product.setCategory(category);
        product.setPrice(BigDecimal.valueOf(dto.getPrice()));
        product.setStock(dto.getStockQuantity());
        product.setStatus(Status.valueOf(dto.getStatus().toUpperCase()));

        return productRepo.save(product);
    }

    public void deleteProduct(Long id) throws ResouceNotFoundException {
        Product product = getProductById(id);
        productRepo.delete(product);
    }

    public Page<Product> getAllProducts(int offset, int pageSize) {
        return productRepo.findAll(PageRequest.of(offset, pageSize));
    }

    public List<Product> searchProduct(String name) {
        return productRepo.findByNameContainingIgnoreCase(name);
    }
    
    
}
