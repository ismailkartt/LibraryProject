package com.tm3library.controller;

import com.tm3library.dto.request.AuthorRequest;
import com.tm3library.dto.request.CategoryRequest;
import com.tm3library.dto.response.AuthorResponse;
import com.tm3library.dto.response.CategoryResponse;
import com.tm3library.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;


    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {

        CategoryResponse categoryResponse = categoryService.createCategory(categoryRequest);

        return ResponseEntity.ok(categoryResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {

        CategoryResponse categoryResponse = categoryService.getCategoryById(id);

        return ResponseEntity.ok(categoryResponse);

    }

    @GetMapping
    public ResponseEntity<Page<CategoryResponse>> getAllCategoriesWithPage(@RequestParam("page") int page,
                                                                           @RequestParam("size") int size,
                                                                           @RequestParam("sort") String prop,
                                                                           @RequestParam(value = "type",
                                                                                   required = false,
                                                                                   defaultValue = "ASC") Sort.Direction type
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(type, prop));

        Page<CategoryResponse> pageResponse = categoryService.findAllWithPage(pageable);

        return ResponseEntity.ok(pageResponse);

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id,
                                                           @Valid @RequestBody CategoryRequest categoryRequest) {

        categoryService.updateCategory(id, categoryRequest);

        CategoryResponse categoryResponse = categoryService.getCategoryById(id);

        return ResponseEntity.ok(categoryResponse);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> deleteCategory(@PathVariable Long id) {

        CategoryResponse categoryResponse = categoryService.removeCategoryById(id);

        return ResponseEntity.ok(categoryResponse);

    }


}
