package com.benson.menu_app.service;

import com.benson.menu_app.exceptions.CategoryNotFoundException;
import com.benson.menu_app.model.Category;
import com.benson.menu_app.model.DTO.request.CategoryRequestDTO;
import com.benson.menu_app.model.DTO.response.CategoryResponseDTO;
import com.benson.menu_app.repository.CategoryRepository;
import com.benson.menu_app.utils.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(@Autowired CategoryRepository categoryRepository, @Autowired CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }


    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        Category category = categoryMapper.toEntity(categoryRequestDTO.name());
        Category saved = categoryRepository.save(category);
        return categoryMapper.toDTO(saved);
    }

    @Override
    public CategoryResponseDTO updateCategory(long id, CategoryRequestDTO categoryRequestDTO) throws CategoryNotFoundException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));

        // apply changes from DTO
        category.setName(categoryRequestDTO.name());
        Category updated = categoryRepository.save(category);
        return categoryMapper.toDTO(updated);
    }

    @Override
    public void deleteCategory(long id) throws CategoryNotFoundException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
        categoryRepository.deleteById(category.getId());
    }

    @Override
    public CategoryResponseDTO getCategory(long id) throws CategoryNotFoundException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
        return categoryMapper.toDTO(category);
    }

    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDTO)
                .toList();
    }
}
