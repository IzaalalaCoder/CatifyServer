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
        return categoryRepository.findById(id).orElseThrow();
    }

    // COMMANDS

    @Transactional
    public void addCategory(Category newCategory) {
        checkCategory(newCategory);
        if (this.categoryRepository.existsByName(newCategory.getName())) {
            throw new AssertionError("this category already exist");
        }
        categoryRepository.save(newCategory);
    }

    @Transactional
    public void updateCategoryOnName(Integer id, String name) {
        if (name == null || name.isEmpty()) {
            throw new AssertionError("Le nom de la catégorie est requis");
        }
        this.categoryRepository.findById(id)
        .map(c -> {
            c.setName(name);
            return this.categoryRepository.save(c);
        })
        .orElseThrow();
    }

    @Transactional
    public void associate(int parentId, int childId) {
        if (parentId == childId) {
            throw new AssertionError("parentId cannot have parentId as child");
        }
        Category catParent = categoryRepository.findById(parentId).orElse(null);
        Category catChild = categoryRepository.findById(childId).orElse(null);

        if (catParent == null || catChild == null) {
            throw new AssertionError("Les catégories parent ou enfant n'existent pas");
        }

        if (catParent.getChild(catChild.getName()) != null) {
            throw new AssertionError("Le parent contient déjà la catégorie enfant à ajouté");
        }

        catChild.setParent(catParent);
        categoryRepository.save(catParent);
        categoryRepository.save(catChild);
    }

    @Transactional
    public void dissociate(int parentId, int childId) {
        Category catParent = categoryRepository.findById(parentId).orElse(null);
        Category catChild = categoryRepository.findById(childId).orElse(null);

        if (catParent == null || catChild == null) {
            throw new AssertionError("Les catégories parent ou enfant n'existent pas");
        }

        if (catParent.getChild(catChild.getName()) == null) {
            throw new AssertionError("Le parent ne contient pas la catégorie enfant à dissocié");
        }

        catChild.setParent(null);
        categoryRepository.save(catParent);
        categoryRepository.save(catChild);
    }

    @Transactional
    public void deleteCategory(int id) {
        Category categoryToDelete = categoryRepository.findById(id).orElseThrow();

        if (categoryToDelete.getParent() != null) {
            categoryToDelete.setParent(null);
        }

        for (Category child : categoryToDelete.getAllChildren()) {
            child.setParent(null);
            categoryRepository.save(child);
        }

        categoryRepository.delete(categoryToDelete);
    }

    public void deleteAll() {
        this.categoryRepository.deleteAll();
    }

    // UTILS

    private void checkCategory(Category category) {
        if (category == null) {
            throw new AssertionError("Category cannot be null");
        }

        if (category.getName() == null || category.getName().isEmpty()) {
            throw new AssertionError("Category name cannot be null or empty");
        }

        if (category.getParent() == category) {
            throw new AssertionError("A category cannot be its own parent");
        }
    }
}
