package com.example.product.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.product.entity.Category;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
	Category findByName(String name);

}
