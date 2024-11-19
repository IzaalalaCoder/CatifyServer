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

    // REQUESTS

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(int id) {
        return categoryRepository.findById(id).orElse(null);
    }

    // COMMANDS

    @Transactional
    public void addCategory(Category newCategory) {
        checkCategory(newCategory);
        for (Category child : new ArrayList<>(newCategory.getAllChildren())) {
            Category cat = this.categoryRepository.findByName(child.getName());
            if (cat != null) {
                newCategory.addChild(cat);
                newCategory.removeChild(child);
            }
        }
        categoryRepository.save(newCategory);
    }

    @Transactional
    public void updateCategory(int id, Category category) {
        checkCategory(category);
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        existingCategory.setName(category.getName());
        List<Category> categories = new ArrayList<>(category.getAllChildren());
        for (Category newChild : categories) {
            Category existingChild = categoryRepository.findByName(newChild.getName());
            if (existingChild != null) {
                existingCategory.addChild(existingChild);
                existingCategory.removeChild(newChild);
            } else {
                categoryRepository.save(newChild);
                existingCategory.addChild(newChild);
            }
        }

        for (Category cat : new ArrayList<>(existingCategory.getAllChildren())) {
            if (category.getChild(cat.getName()) == null) {
                existingCategory.removeChild(cat);
            }
        }

        categoryRepository.save(existingCategory);
    }

    @Transactional
    public void associate(int parentId, int childId) {
        Category catParent = categoryRepository.findById(parentId).orElse(null);
        Category catChild = categoryRepository.findById(childId).orElse(null);

        if (catParent != null && catChild != null) {
            catChild.setParent(catParent);
            categoryRepository.save(catParent);
            categoryRepository.save(catChild);
        }
    }

    @Transactional
    public void deleteCategory(int id) {
        Category categoryToDelete = categoryRepository.findById(id).orElse(null);

        if (categoryToDelete == null) {
            return;
        }

        for (Category child : categoryToDelete.getAllChildren()) {
            deleteCategory(child.getId());  // Recursively delete children
        }

        categoryRepository.delete(categoryToDelete);
    }

    public void deleteAll() {
        this.categoryRepository.deleteAll();
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
            throw new IllegalArgumentException("A category cannot be its own parent");
        }
    }
}
