package univ.rouen.fr.catify.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import univ.rouen.fr.catify.server.entity.Category;
import univ.rouen.fr.catify.server.repository.CategoryRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SearchCategoryService {

    // ATTRIBUTES

    @Autowired
    private CategoryRepository categoryRepository;

    // REQUESTS

    public Page<Category> searchCategories(Boolean root, Date afterDate, Date beforeDate, Pageable pageable) {
        if (afterDate != null && beforeDate != null && afterDate.after(beforeDate)) {
            throw new IllegalArgumentException("The 'after' date cannot be after the 'before' date.");
        }
        return categoryRepository.findCategoriesByFilters(root, afterDate, beforeDate, pageable);
    }

    public List<Category> searchCategories(Boolean root, Date afterDate, Date beforeDate) {
        List<Category> categories = categoryRepository.findAll();

        if (root != null) {
            categories = filterByRoot(categories, root);
        }
        if (afterDate != null) {
            categories = filterByAfterDate(categories, afterDate);
        }
        if (beforeDate != null) {
            categories = filterByBeforeDate(categories, beforeDate);
        }

        return categories;
    }

    // UTILS

    private List<Category> filterByRoot(List<Category> categories, Boolean root) {
        List<Category> filtered = new ArrayList<>();
        for (Category category : categories) {
            if (category.isRoot() == root) {
                filtered.add(category);
            }
        }
        return filtered;
    }

    private List<Category> filterByAfterDate(List<Category> categories, Date afterDate) {
        List<Category> filtered = new ArrayList<>();
        for (Category category : categories) {
            if (category.getDateOfCreation().after(afterDate)) {
                filtered.add(category);
            }
        }
        return filtered;
    }

    private List<Category> filterByBeforeDate(List<Category> categories, Date beforeDate) {
        List<Category> filtered = new ArrayList<>();
        for (Category category : categories) {
            if (category.getDateOfCreation().before(beforeDate)) {
                filtered.add(category);
            }
        }
        return filtered;
    }
}