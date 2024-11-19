package univ.rouen.fr.catify.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import univ.rouen.fr.catify.server.entity.Category;
import univ.rouen.fr.catify.server.service.CategoryService;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "/category")
public class CategoryController {

    // ATTRIBUTES

    @Autowired
    private CategoryService categoryService;

    // MAPPINGS

    @GetMapping(value = "/{id}", produces = "application/json")
    public Category getCategory(@PathVariable int id) {
        return categoryService.getCategoryById(id);
    }

    @GetMapping(value = "/associate/{parent}/{child}")
    public void associateChildAtParent(@PathVariable int parent, @PathVariable int child) {
        this.categoryService.associate(parent, child);
    }

    @GetMapping(value = "/{id}/childrens", produces = "application/json")
    public List<Category> getChildrensOfCategory(@PathVariable int id) {
        return categoryService.getCategoryById(id).getAllChildren();
    }

    @PutMapping(value = "/update/{id}", consumes = "application/json")
    public void updateCategory(@PathVariable int id, @RequestBody Category category) {
        categoryService.updateCategory(id, category);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCategory(@PathVariable int id) {
        categoryService.deleteCategory(id);
    }
}