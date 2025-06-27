package com.example.product.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.product.dto.ProductDto;
import com.example.product.entity.Product;

public interface ProductService {
	Product createProduct(ProductDto dto);

	Product getProductById(Long id);

	Product updateProduct(Long id, ProductDto dto);

	void deleteProduct(Long id);

	List<Product> searchProduct(String field);

	Page<Product> getAllProducts(int offset, int pagesize);

}
