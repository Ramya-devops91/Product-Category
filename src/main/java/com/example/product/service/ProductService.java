package com.example.product.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.example.product.dto.ProductDto;
import com.example.product.entity.Product;

import jakarta.servlet.http.HttpServletResponse;

public interface ProductService {
	Product createProduct(ProductDto dto);

	Product getProductById(Long id);

	Product updateProduct(Long id, ProductDto dto);

	void deleteProduct(Long id);

	List<Product> searchProduct(String field);

	Page<Product> getAllProducts(int offset, int pagesize);

	Map<String, Object> importProducts(MultipartFile file);

	ResponseEntity<?> importProductsFromExcel(MultipartFile file);

	void downloadExcelTemplate(HttpServletResponse response) throws IOException;

}
