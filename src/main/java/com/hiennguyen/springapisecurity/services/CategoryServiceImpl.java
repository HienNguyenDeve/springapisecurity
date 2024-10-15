package com.hiennguyen.springapisecurity.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hiennguyen.springapisecurity.dtos.category.CategoryCreateUpdateDTO;
import com.hiennguyen.springapisecurity.dtos.category.CategoryDTO;
import com.hiennguyen.springapisecurity.entities.Category;
import com.hiennguyen.springapisecurity.repositories.CategoryRepository;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository _categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this._categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDTO> findAll() {
        // Get all category entities
        var categories = _categoryRepository.findAll();

        // Convert category entities to category DTOs

        var categoryDTOs = categories.stream().map(category -> {
            var categoryDTO = new CategoryDTO();
            categoryDTO.setId(category.getId());
            categoryDTO.setName(category.getName());
            categoryDTO.setDescription(category.getDescription());
            categoryDTO.setActive(category.isActive());
            return categoryDTO;
        }).toList();

        return categoryDTOs;
    }

    @Override
    public CategoryDTO findById(UUID id) {
        // Get category entity by id
        var category = _categoryRepository.findById(id).orElse(null);

        // If category entity is null, return null
        if (category == null) {
            return null;
        }

        // Convert category entity to category DTO

        var categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setDescription(category.getDescription());
        categoryDTO.setActive(category.isActive());

        return categoryDTO;
    }

    @Override
    public CategoryDTO create(CategoryCreateUpdateDTO categoryCreateUpdateDTO) {
        // Check if category create update DTO is null
        if (categoryCreateUpdateDTO == null) {
            throw new IllegalArgumentException("Category is required");
        }

        // Check category with the same name exists
        var existingCategory = _categoryRepository.findByName(categoryCreateUpdateDTO.getName());

        if (existingCategory != null) {
            throw new IllegalArgumentException("Category with the same name already exists");
        }

        var category = new Category();
        category.setName(categoryCreateUpdateDTO.getName());
        category.setDescription(categoryCreateUpdateDTO.getDescription());
        category.setActive(categoryCreateUpdateDTO.isActive());
        category.setDeleted(false); // Create, setDeleted is false
        category.setCreatedAt(LocalDateTime.now()); // Create, setCreatedAt is now

        // Save category entity
        category = _categoryRepository.save(category);

        // Convert category entity to category DTO
        var categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setDescription(category.getDescription());
        categoryDTO.setActive(category.isActive());

        return categoryDTO;
    }

    @Override
    public CategoryDTO update(UUID id, CategoryCreateUpdateDTO categoryCreateUpdateDTO) {
        // Check if category create update DTO is null
        if (categoryCreateUpdateDTO == null) {
            throw new IllegalArgumentException("Category is required");
        }

        // Get category entity by id
        var category = _categoryRepository.findById(id).orElse(null);
        if (category == null) {
            throw new IllegalArgumentException("Category not found");
        }

        // Check category with the same name exists
        var existingCategory = _categoryRepository.findByName(categoryCreateUpdateDTO.getName());

        if (existingCategory != null && !existingCategory.getId().equals(id)) {
            throw new IllegalArgumentException("Category with the same name already exists");
        }

        category.setName(categoryCreateUpdateDTO.getName());
        category.setDescription(categoryCreateUpdateDTO.getDescription());
        category.setActive(categoryCreateUpdateDTO.isActive());
        category.setUpdatedAt(LocalDateTime.now()); // Update, setUpdatedAt is now

        // Save category entity
        category = _categoryRepository.save(category);

        // Convert category entity to category DTO
        var categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setDescription(category.getDescription());
        categoryDTO.setActive(category.isActive());

        return categoryDTO;
    }

    @Override
    public boolean delete(UUID id) {
        var existingCategory = _categoryRepository.findById(id).orElse(null);

        if (existingCategory == null) {
            throw new IllegalArgumentException("Category not found");
        }

        _categoryRepository.delete(existingCategory);

        // Check if category entity is deleted
        var isDeleted = _categoryRepository.findById(id).isEmpty();

        return isDeleted;
    }

    @Override
    public boolean delete(UUID id, boolean isSoftDelete) {
        var existingCategory = _categoryRepository.findById(id).orElse(null);

        if (existingCategory == null) {
            throw new IllegalArgumentException("Category not found");
        }

        if (isSoftDelete) {
            existingCategory.setDeleted(true);
            existingCategory.setDeletedAt(LocalDateTime.now());
            _categoryRepository.save(existingCategory);

            // Check if category entity is soft deleted
            var deletedCategory = _categoryRepository.findById(id).orElse(null);
            if (deletedCategory != null && deletedCategory.isDeleted()) {
                return true;
            } else {
                return false;
            }
        } else {
            
            _categoryRepository.delete(existingCategory);
            // Check if category entity is deleted
            var isDeleted = _categoryRepository.findById(id).isEmpty();

            return isDeleted;
        }
    }
}
