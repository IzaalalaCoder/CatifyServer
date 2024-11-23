package univ.rouen.fr.catify.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import univ.rouen.fr.catify.server.entity.Category;
import univ.rouen.fr.catify.server.service.CategoryService;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "/category")
public class CategoryController {

    // ATTRIBUTES

    @Autowired
    private CategoryService categoryService;

    // MAPPINGS

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Category> getCategory(@PathVariable int id) {
        try {
            Category c = categoryService.getCategoryById(id);
            return ResponseEntity.ok(c);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping(value = "/associate/{parent}/{child}", produces = "application/json")
    public ResponseEntity<String> associateChildAtParent(@PathVariable int parent, @PathVariable int child) {
        try {
            this.categoryService.associate(parent, child);
            return ResponseEntity.ok("Les catégories sont maintenant associées");
        } catch (AssertionError e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping(value = "/dissociate/{parent}/{child}", produces = "application/json")
    public ResponseEntity<String> dissociateChildAtParent(@PathVariable int parent, @PathVariable int child) {
        try {
            this.categoryService.dissociate(parent, child);
            return ResponseEntity.ok("Les catégories sont maintenant dissociées");
        } catch (AssertionError e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping(value = "/{id}/childrens", produces = "application/json")
    public ResponseEntity<List<Category>> getChildrensOfCategory(@PathVariable int id) {
        try {
            Category c = categoryService.getCategoryById(id);
            return ResponseEntity.ok(c.getAllChildren());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping(value = "/update/{id}/name/{name}")
    public ResponseEntity<String> updateCategoryOnName(@PathVariable int id, @PathVariable String name) {
        try {
            categoryService.updateCategoryOnName(id, name);
            return ResponseEntity.ok("La catégorie a bien changé de nom");
        } catch (AssertionError e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La catégorie est inexistante");
        }
    }

    @DeleteMapping(value = "/delete/{id}", produces = "application/json")
    public ResponseEntity<String> deleteCategory(@PathVariable int id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok("La catégorie est supprimée");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La catégorie n'existe pas");
        }
    }
}