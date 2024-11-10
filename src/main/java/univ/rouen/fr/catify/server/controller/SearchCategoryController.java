package univ.rouen.fr.catify.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import univ.rouen.fr.catify.server.entity.Category;
import univ.rouen.fr.catify.server.service.SearchCategoryService;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("categories/")
public class SearchCategoryController {

    // ATTRIBUTES

    @Autowired
    private SearchCategoryService searchCategoryService;

    // MAPPINGS

    @GetMapping("search")
    public Page<Category> searchCategories(
            @RequestParam(value = "root", required = false) Boolean root,
            @RequestParam(value = "after", required = false) Date afterDate,
            @RequestParam(value = "before", required = false) Date beforeDate,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sort", defaultValue = "dateOfCreation") String sortBy,
            @RequestParam(value = "order", defaultValue = "asc") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        Pageable pageable = PageRequest.of(page, 10, direction, sortBy); // Initialisation du Pageable

        List<Category> categoryList = searchCategoryService.searchCategories(root, afterDate, beforeDate);
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
                throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        }
        int start = page * 10;
        int end = Math.min((start + 10), categoryList.size());
        List<Category> paginatedList = categoryList.subList(start, end);

        return new PageImpl<>(paginatedList, pageable, categoryList.size());
    }
}