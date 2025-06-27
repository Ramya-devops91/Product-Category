package com.example.product.service;

import java.util.List;

import com.example.product.entity.Category;
import com.example.product.exception.ResouceNotFoundException;

public interface CategoryService {
	List<Category> getAllCategories();

	Category getCategoryById(Long id) throws ResouceNotFoundException;

	Category createCategory(Category category);

	Category updateCategory(Long id, Category category) throws ResouceNotFoundException;

	void deleteCategory(Long id) throws ResouceNotFoundException;

}
