package com.hiennguyen.springapisecurity.services;

import java.util.List;
import java.util.UUID;

import com.hiennguyen.springapisecurity.dtos.category.CategoryCreateUpdateDTO;
import com.hiennguyen.springapisecurity.dtos.category.CategoryDTO;

public interface CategoryService {
    List<CategoryDTO> findAll();

    CategoryDTO findById(UUID id);

    CategoryDTO create(CategoryCreateUpdateDTO categoryCreateUpdateDTO);

    CategoryDTO update(UUID id, CategoryCreateUpdateDTO categoryCreateUpdateDTO);

    boolean delete(UUID id);

    // If isSoftDelete is true, perform soft delete => set isDeleted = true, deletedAt = current date time
    // If isSoftDelete is false, perform hard delete => delete from database
    boolean delete(UUID id, boolean isSoftDelete);
}
