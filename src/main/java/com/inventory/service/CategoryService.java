package com.inventory.service;

import com.inventory.dto.request.CategoryRequest;
import com.inventory.entity.Category;
import com.inventory.exception.CategoryException;
import com.inventory.repo.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryException("CAT001", "Category not found", HttpStatus.NOT_FOUND));
    }

    public Category createCategory(String name, String description) {
        if (categoryRepository.findByNameIgnoreCase(name).isPresent()) {
            throw new CategoryException("CAT002", "Category with this name already exists", HttpStatus.BAD_REQUEST);
        }
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        category.setActive(true);
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, CategoryRequest categoryRequest) {
        Category category = getCategoryById(id);
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        category.setActive(category.isActive());
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        categoryRepository.delete(category);
    }
}
