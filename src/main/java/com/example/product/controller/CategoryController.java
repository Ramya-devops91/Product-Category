package com.example.product.controller;

import com.example.product.entity.Category;
import com.example.product.exception.ResouceNotFoundException;
import com.example.product.service.CategoryService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@GetMapping
	public ResponseEntity<List<Category>> getAllCategories() {
		return ResponseEntity.ok(categoryService.getAllCategories());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Category> getCategoryById(@PathVariable Long id) throws ResouceNotFoundException {
		return ResponseEntity.ok(categoryService.getCategoryById(id));
	}

	@PostMapping
	public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) {
		return new ResponseEntity<>(categoryService.createCategory(category), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Category> updateCategory(@PathVariable Long id, @Valid @RequestBody Category category)
			throws ResouceNotFoundException {
		return ResponseEntity.ok(categoryService.updateCategory(id, category));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCategory(@PathVariable Long id) throws ResouceNotFoundException {
		categoryService.deleteCategory(id);
		return ResponseEntity.noContent().build();
	}
}
