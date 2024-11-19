package univ.rouen.fr.catify.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import univ.rouen.fr.catify.server.entity.Category;
import univ.rouen.fr.catify.server.service.CategoryService;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "/categories")
public class CategoriesController {

    // ATTRIBUTES

    @Autowired
    private CategoryService categoryService;

    // MAPPINGS

    @GetMapping(produces = "application/json")
    public List<Category> getCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping(path = "/add", consumes = "application/json")
    public void addCategory(@RequestBody Category category) {
        categoryService.addCategory(category);
    }
}