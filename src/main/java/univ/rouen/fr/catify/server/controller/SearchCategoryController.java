package univ.rouen.fr.catify.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import univ.rouen.fr.catify.server.entity.Category;
import univ.rouen.fr.catify.server.service.SearchCategoryService;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("categories/")
public class SearchCategoryController {

    // ATTRIBUTES

    @Autowired
    private SearchCategoryService searchCategoryService;

    // MAPPINGS

    @GetMapping(value = "search", produces = "application/json")
    public ResponseEntity<Map<String, Object>> searchCategories(
            @RequestParam(value = "root", required = false) Boolean root,
            @RequestParam(value = "after", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date afterDate,
            @RequestParam(value = "before", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") Date beforeDate,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sort", defaultValue = "dateOfCreation") String sortBy,
            @RequestParam(value = "order", defaultValue = "asc") String sortDirection) {

        try {
            Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
            List<Category> categoryList = searchCategoryService.searchCategories(root, afterDate, beforeDate);

            if (sortBy != null && !sortBy.isEmpty()) {
                this.sortListBySpecificSort(categoryList, sortBy, direction);
            }

            int pageSize = 10;
            int start = page * pageSize;
            int end = Math.min(start + pageSize, categoryList.size());
            List<Category> paginatedList = categoryList.subList(start, end);

            int totalElements = categoryList.size();
            int totalPages = (int) Math.ceil((double) totalElements / pageSize);

            Map<String, Object> response = new HashMap<>();
            response.put("categories", paginatedList);
            response.put("totalPages", totalPages);
            response.put("currentPage", page);

            return ResponseEntity.ok(response);
        } catch (AssertionError e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // UTILS

    private void sortListBySpecificSort(List<Category> categoryList, String sortBy, Sort.Direction direction) {
        switch (sortBy.toLowerCase()) {
            case "id":
                categoryList.sort((c1, c2) -> {
                    int comparison = c1.getId().compareTo(c2.getId());
                    return direction.isAscending() ? comparison : -comparison;
                });
                break;
            case "dateofcreation":
                categoryList.sort((c1, c2) -> {
                    int comparison = c1.getDateOfCreation().compareTo(c2.getDateOfCreation());
                    return direction.isAscending() ? comparison : -comparison;
                });
                break;
            case "name":
                categoryList.sort((c1, c2) -> {
                    int comparison = c1.getName().compareTo(c2.getName());
                    return direction.isAscending() ? comparison : -comparison;
                });
                break;
            case "numberchildren":
                categoryList.sort((c1, c2) -> {
                    int comparison = Integer.compare(c1.getNumberChildren(), c2.getNumberChildren());
                    return direction.isAscending() ? comparison : -comparison;
                });
                break;
            default:
                break;
        }
    }
}