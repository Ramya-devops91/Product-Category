package com.example.product.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.example.product.dto.ProductDto;
import com.example.product.entity.Product;
import com.example.product.exception.ResouceNotFoundException;

import jakarta.servlet.http.HttpServletResponse;

public interface ProductService {
	Product createProduct(ProductDto dto) throws ResouceNotFoundException;

	Product getProductById(Long id) throws ResouceNotFoundException;

	Product updateProduct(Long id, ProductDto dto) throws ResouceNotFoundException;

	void deleteProduct(Long id) throws ResouceNotFoundException;

	List<Product> searchProduct(String field);

	Page<Product> getAllProducts(int offset, int pagesize);

	Map<String, Object> importProducts(MultipartFile file);

	ResponseEntity<?> importProductsFromExcel(MultipartFile file);

	void downloadExcelTemplate(HttpServletResponse response) throws IOException;

}
