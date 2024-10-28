package univ.rouen.fr.catify.server.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import univ.rouen.fr.catify.server.entity.Category;
import univ.rouen.fr.catify.server.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return new ArrayList<>(categoryRepository.findAll());
    }

    public Category getCategoryById(int id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteCategory(int id) {
        Category categoryToDelete = categoryRepository.findById(id).orElse(null);

        if (categoryToDelete == null) {
            return;
        }

        for (Category child : categoryToDelete.getAllChildren()) {
            deleteCategory(child.getId());
        }

        categoryRepository.delete(categoryToDelete);
    }

    @Transactional
    public void updateCategory(int id, Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }

        if (category.getName() == null || category.getName().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }

        categoryRepository.findById(id).ifPresent(updated -> {
            if (!categoryRepository.existsByName(category.getName()) || category.getName().equals(updated.getName())) {
                updated.copyFrom(category);
                categoryRepository.save(updated); // Sauvegarder les modifications
            } else {
                throw new IllegalArgumentException("A category with this name already exists");
            }
        });
    }


    @Transactional
    public void addCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }

        if (category.getName() == null || category.getName().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }

        if (!categoryRepository.existsByName(category.getName())) {
            categoryRepository.save(category);
        }
    }
}