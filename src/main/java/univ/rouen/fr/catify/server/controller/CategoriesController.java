package univ.rouen.fr.catify.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @DeleteMapping(path="/deleteAll", produces = "application/json")
    public ResponseEntity<String> deleteAll() {
        categoryService.deleteAll();
        return ResponseEntity.ok("{\"message\" : \"Les catégories ont bien été supprimés\"}");
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Category>> getCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PostMapping(path = "/add", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> addCategory(@RequestBody Category category) {
        try {
            categoryService.addCategory(category);
        } catch (AssertionError e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\" : \"Le nom de la catégorie est déjà pris\"}");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\": \"La catégorie a bien été créée\"}");
    }
}