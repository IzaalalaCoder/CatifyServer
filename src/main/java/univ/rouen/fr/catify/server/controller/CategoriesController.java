package univ.rouen.fr.catify.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import univ.rouen.fr.catify.server.entity.Category;
import univ.rouen.fr.catify.server.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping(path = "/categories")
public class CategoriesController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping(produces = "application/json")
    public List<Category> getCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping(path = "/add", consumes = "application/json")
    public void addCategory(@RequestBody Category category) {
        categoryService.addCategory(category);
    }

}