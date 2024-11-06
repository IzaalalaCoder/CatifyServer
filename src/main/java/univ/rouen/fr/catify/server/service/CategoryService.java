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
        checkCategory(category);

        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        existingCategory.setName(category.getName());

        List<Category> updatedChildren = new ArrayList<>();
        for (Category child : category.getAllChildren()) {
            if (child.getId() == null) {
                child.setParent(existingCategory);
                categoryRepository.save(child);
                updatedChildren.add(child);
            } else {
                Category existingChild = categoryRepository.findById(child.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Child category not found"));
                existingChild.setName(child.getName());
                existingChild.setParent(existingCategory);
                categoryRepository.save(existingChild);
                updatedChildren.add(existingChild);
            }
        }

        for (Category currentChild : existingCategory.getAllChildren()) {
            if (!updatedChildren.contains(currentChild)) {
                currentChild.setParent(null);
            }
        }

        categoryRepository.save(existingCategory);
    }

    @Transactional
    public void addCategory(Category category) {
        checkCategory(category);

        if (!categoryRepository.existsByName(category.getName())) {
            categoryRepository.save(category);
        }

        for (Category c : category.getAllChildren()) {
            if (!categoryRepository.existsByName(c.getName())) {
                c.setParent(category);
                categoryRepository.save(c);
            } else {
                Category cat = categoryRepository.findByName(c.getName());
                cat.setParent(category);
                categoryRepository.save(cat);
            }
        }
    }

    // UTILS

    private void checkCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }

        if (category.getName() == null || category.getName().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }

        if (category.getParent() == category) {
            throw new IllegalArgumentException("A category cannot be its own parent.");
        }
    }
}