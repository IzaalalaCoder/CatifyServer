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
import java.util.NoSuchElementException;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "/category")
public class CategoryController {

    // ATTRIBUTES

    @Autowired
    private CategoryService categoryService;

    // MAPPINGS

    @Operation(
        description = "Get specific category",
        summary = "Get one category by its id",
        responses = {
            @ApiResponse(
                description = "Successfully return category",
                responseCode = "200",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Category.class)
                )
            ),
            @ApiResponse(
                description = "Category not found",
                responseCode = "404",
                content = @Content(
                    mediaType = "application/json"
                )
            )
        }
    )
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Category> getCategory(@PathVariable int id) {
        try {
            Category c = categoryService.getCategoryById(id);
            return ResponseEntity.ok(c);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(
        description = "Get all child categories of a specific category",
        summary = "Get children of category",
        responses = {
            @ApiResponse(
                description = "Successfully returned child categories",
                responseCode = "200",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Category.class)
                )
            ),
            @ApiResponse(
                description = "Category not found",
                responseCode = "404",
                content = @Content(
                    mediaType = "application/json"
                )
            )
        }
    )
    @GetMapping(value = "/{id}/childrens", produces = "application/json")
    public ResponseEntity<List<Category>> getChildrensOfCategory(@PathVariable int id) {
        try {
            Category c = categoryService.getCategoryById(id);
            return ResponseEntity.ok(c.getAllChildren());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(
            description = "Associate a child category to a parent category",
            summary = "Associate categories",
            responses = {
                    @ApiResponse(
                            description = "Categories successfully associated",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            description = "Conflict: Categories cannot be associated",
                            responseCode = "409",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    @PutMapping(value = "/associate/{parent}/{child}", produces = "application/json")
    public ResponseEntity<String> associateChildAtParent(@PathVariable int parent, @PathVariable int child) {
        try {
            this.categoryService.associate(parent, child);
            return ResponseEntity.ok("{\"message\":\"Les catégories sont maintenant associées\"}");
        } catch (AssertionError e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @Operation(
            description = "Dissociate a child category from its parent category",
            summary = "Dissociate categories",
            responses = {
                    @ApiResponse(
                            description = "Categories successfully dissociated",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            description = "Conflict: Categories cannot be dissociated",
                            responseCode = "409",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    @PutMapping(value = "/dissociate/{parent}/{child}", produces = "application/json")
    public ResponseEntity<String> dissociateChildAtParent(@PathVariable int parent, @PathVariable int child) {
        try {
            this.categoryService.dissociate(parent, child);
            return ResponseEntity.ok("{\"message\":\"Les catégories sont maintenant dissociées\"}");
        } catch (AssertionError e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @Operation(
        description = "Update the name of a category",
        summary = "Update category name",
        responses = {
            @ApiResponse(
                description = "Category name successfully updated",
                responseCode = "200",
                content = @Content(
                    mediaType = "application/json"
                )
            ),
            @ApiResponse(
                description = "Conflict: Invalid data for category update",
                responseCode = "409",
                content = @Content(
                    mediaType = "application/json"
                )
            ),
            @ApiResponse(
                description = "Category not found",
                responseCode = "404",
                content = @Content(
                    mediaType = "application/json"
                )
            )
        }
    )
    @PutMapping(value = "/update/{id}/name/{name}")
    public ResponseEntity<String> updateCategoryOnName(@PathVariable int id, @PathVariable String name) {
        try {
            categoryService.updateCategoryOnName(id, name);
            return ResponseEntity.ok("{\"message\":\"La catégorie a bien changé de nom\"}");
        } catch (AssertionError e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"La catégorie est inexistante\"}");
        }
    }

    @Operation(
        description = "Delete a category by its ID",
        summary = "Delete category",
        responses = {
            @ApiResponse(
                description = "Category successfully deleted",
                responseCode = "200",
                content = @Content(
                    mediaType = "application/json"
                )
            ),
            @ApiResponse(
                description = "Category not found",
                responseCode = "404",
                content = @Content(
                    mediaType = "application/json"
                )
            )
        }
    )
    @DeleteMapping(value = "/delete/{id}", produces = "application/json")
    public ResponseEntity<String> deleteCategory(@PathVariable int id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok("{\"message\":\"La catégorie est supprimée\"}");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"La catégorie n'existe pas\"}");
        }
    }
}