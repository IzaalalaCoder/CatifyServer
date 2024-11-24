package univ.rouen.fr.catify.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
        description = "Retrieve all categories from the system",
        summary = "Get a list of all categories",
        responses = {
            @ApiResponse(
                description = "Successfully retrieved all categories",
                responseCode = "200",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Category.class)
                )
            )
        }
    )
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Category>> getCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @Operation(
        description = "Add new category in the system",
        summary = "Add one category",
        responses = {
            @ApiResponse(
                description = "Successfully added new category",
                responseCode = "201",
                content = @Content(
                    mediaType = "application/json"
                )
            ),
            @ApiResponse(
                description = "Failed added new category",
                responseCode = "409",
                content = @Content(
                    mediaType = "application/json"
                )
            )
        }
    )
    @PostMapping(path = "/add", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> addCategory(@RequestBody Category category) {
        try {
            categoryService.addCategory(category);
        } catch (AssertionError e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\" : \"Le nom de la catégorie est déjà pris\"}");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\": \"La catégorie a bien été créée\"}");
    }

    @Operation(
            description = "Delete all categories from the system",
            summary = "Deletes all categories",
            responses = {
                    @ApiResponse(
                            description = "All categories removed successfully",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    @DeleteMapping(path="/deleteAll", produces = "application/json")
    public ResponseEntity<String> deleteAll() {
        categoryService.deleteAll();
        return ResponseEntity.ok("{\"message\" : \"Les catégories ont bien été supprimés\"}");
    }
}