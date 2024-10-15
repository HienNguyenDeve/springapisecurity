package com.hiennguyen.springapisecurity.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.hiennguyen.springapisecurity.dtos.category.*;
import com.hiennguyen.springapisecurity.services.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "categories", description = "Category APIs")
public class CategoryController {
    private final CategoryService _categoryService;

    public CategoryController(CategoryService categoryService) {
        this._categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "Get all categories")
    public ResponseEntity<List<CategoryDTO>> findAll() {
        var categories = _categoryService.findAll();

        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by id")
    public ResponseEntity<CategoryDTO> findById(@PathVariable("id") UUID id) {
        // Get category by id
        var category = _categoryService.findById(id);

        // Check if category is null, return 404 Not Found
        if (category == null) {
            return ResponseEntity.notFound().build();
        }

        // Return 200 OK with category
        return ResponseEntity.ok(category);
    }

    @PostMapping
    @Operation(summary = "Create category")
    public ResponseEntity<?> create(@Valid @RequestBody CategoryCreateUpdateDTO categoryCreateUpdateDTO,
            BindingResult bindingResult) {

        // Check category is valid
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        // Create category
        var category = _categoryService.create(categoryCreateUpdateDTO);

        // Return 201 Created with category
        return ResponseEntity.status(201).body(category);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update category by id")
    public ResponseEntity<?> update(@PathVariable("id") UUID id,
            @Valid @RequestBody CategoryCreateUpdateDTO categoryCreateUpdateDTO,
            BindingResult bindingResult) {

        // Check category is valid
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        // Create category
        var category = _categoryService.update(id, categoryCreateUpdateDTO);

        // Return 201 Created with category
        return ResponseEntity.status(201).body(category);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category by id")
    public ResponseEntity<?> delete(@PathVariable("id") UUID id) {
        // Delete category
        var isDeleted = _categoryService.delete(id);

        // Check if category is deleted
        if (!isDeleted) {
            return ResponseEntity.notFound().build();
        }

        // Return 204 No Content
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/soft-delete")
    @Operation(summary = "Soft delete category by id")
    public ResponseEntity<?> delete(@PathVariable("id") UUID id,
            @RequestParam(value = "isSoftDelete", defaultValue = "true") boolean isSoftDelete) {
        // Delete category
        var isDeleted = _categoryService.delete(id, isSoftDelete);

        // Check if category is deleted
        if (!isDeleted) {
            return ResponseEntity.notFound().build();
        }

        // Return 204 No Content
        return ResponseEntity.noContent().build();
    }
}
