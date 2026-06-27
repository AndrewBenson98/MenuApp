package com.benson.menu_app.service;

import com.benson.menu_app.exceptions.CategoryNotFoundException;
import com.benson.menu_app.model.DTO.request.CategoryRequestDTO;
import com.benson.menu_app.model.DTO.response.CategoryResponseDTO;

import java.util.List;

public interface CategoryService {

    CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO);
    CategoryResponseDTO updateCategory(long id, CategoryRequestDTO categoryRequestDTO) throws CategoryNotFoundException;
    void deleteCategory(long id) throws CategoryNotFoundException;
    CategoryResponseDTO getCategory(long id) throws CategoryNotFoundException;
    List<CategoryResponseDTO> getAllCategories();


}
