package com.example.product.service;

import com.example.product.entity.Category;
import com.example.product.repo.CategoryRepo;
import com.example.product.exception.ResouceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) throws ResouceNotFoundException {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException("Category not found with id " + id));
    }

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long id, Category updatedCategory) throws ResouceNotFoundException {
        Category existing = getCategoryById(id);
        existing.setName(updatedCategory.getName());
        existing.setDescription(updatedCategory.getDescription());
        return categoryRepository.save(existing);
    }

    @Override
    public void deleteCategory(Long id) throws ResouceNotFoundException {
        Category existing = getCategoryById(id);
        categoryRepository.delete(existing);
    }
}
