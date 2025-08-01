package com.example.product.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.product.dto.ProductDto;
import com.example.product.entity.Product;
import com.example.product.exception.ResouceNotFoundException;
import com.example.product.service.ProductService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	@PostMapping
	public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDto dto) throws ResouceNotFoundException {
		return new ResponseEntity<>(productService.createProduct(dto), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDto dto) throws ResouceNotFoundException {
		return ResponseEntity.ok(productService.updateProduct(id, dto));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable Long id) throws ResouceNotFoundException {
		return ResponseEntity.ok(productService.getProductById(id));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id) throws ResouceNotFoundException {
		productService.deleteProduct(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping
	public ResponseEntity<Page<Product>> getAllProducts(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<Product> products = productService.getAllProducts(page, size);
		return ResponseEntity.ok(products);
	}

	@GetMapping("/search")
	public ResponseEntity<List<Product>> searchProductsByName(@RequestParam String name) {
		return ResponseEntity.ok(productService.searchProduct(name));
	}

	@PostMapping("/import")
	public ResponseEntity<?> importExcel(@RequestParam("file") MultipartFile file) {
		return productService.importProductsFromExcel(file);
	}

	@GetMapping("/template")
	public void downloadTemplate(HttpServletResponse response) throws IOException {
		productService.downloadExcelTemplate(response);
	}

}
