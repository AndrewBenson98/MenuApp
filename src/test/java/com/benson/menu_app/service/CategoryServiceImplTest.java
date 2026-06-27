package com.benson.menu_app.service;

import com.benson.menu_app.exceptions.CategoryNotFoundException;
import com.benson.menu_app.model.Category;
import com.benson.menu_app.model.DTO.request.CategoryRequestDTO;
import com.benson.menu_app.model.DTO.response.CategoryResponseDTO;
import com.benson.menu_app.repository.CategoryRepository;
import com.benson.menu_app.utils.CategoryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CategoryService Tests")
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    private CategoryServiceImpl categoryService;

    private Category testCategory;
    private CategoryRequestDTO testRequestDTO;
    private CategoryResponseDTO testResponseDTO;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryServiceImpl(categoryRepository, categoryMapper);

        // Setup test data
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Appetizers");

        testRequestDTO = new CategoryRequestDTO("Appetizers");

        testResponseDTO = new CategoryResponseDTO(1L, "Appetizers");
    }

    @Test
    @DisplayName("Should create a new category successfully")
    void testCreateCategory_Success() {
        // Arrange
        when(categoryMapper.toEntity("Appetizers")).thenReturn(testCategory);
        when(categoryRepository.save(testCategory)).thenReturn(testCategory);
        when(categoryMapper.toDTO(testCategory)).thenReturn(testResponseDTO);

        // Act
        CategoryResponseDTO result = categoryService.createCategory(testRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Appetizers", result.name());
        verify(categoryRepository, times(1)).save(testCategory);
        verify(categoryMapper, times(1)).toEntity("Appetizers");
        verify(categoryMapper, times(1)).toDTO(testCategory);
    }

    @Test
    @DisplayName("Should retrieve all categories successfully")
    void testGetAllCategories_Success() {
        // Arrange
        List<Category> categories = new ArrayList<>();
        categories.add(testCategory);

        Category testCategory2 = new Category();
        testCategory2.setId(2L);
        testCategory2.setName("Main Courses");
        categories.add(testCategory2);

        CategoryResponseDTO responseDTO2 = new CategoryResponseDTO(2L, "Main Courses");

        when(categoryRepository.findAll()).thenReturn(categories);
        when(categoryMapper.toDTO(testCategory)).thenReturn(testResponseDTO);
        when(categoryMapper.toDTO(testCategory2)).thenReturn(responseDTO2);

        // Act
        List<CategoryResponseDTO> result = categoryService.getAllCategories();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Appetizers", result.get(0).name());
        assertEquals("Main Courses", result.get(1).name());
        verify(categoryRepository, times(1)).findAll();
        verify(categoryMapper, times(2)).toDTO(any(Category.class));
    }

    @Test
    @DisplayName("Should return empty list when no categories exist")
    void testGetAllCategories_Empty() {
        // Arrange
        when(categoryRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<CategoryResponseDTO> result = categoryService.getAllCategories();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should retrieve category by id successfully")
    void testGetCategory_Success() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(categoryMapper.toDTO(testCategory)).thenReturn(testResponseDTO);

        // Act
        CategoryResponseDTO result = categoryService.getCategory(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Appetizers", result.name());
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryMapper, times(1)).toDTO(testCategory);
    }

    @Test
    @DisplayName("Should throw CategoryNotFoundException when category does not exist")
    void testGetCategory_NotFound() {
        // Arrange
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        CategoryNotFoundException exception = assertThrows(
                CategoryNotFoundException.class,
                () -> categoryService.getCategory(999L)
        );
        assertTrue(exception.getMessage().contains("Category not found with id: 999"));
        verify(categoryRepository, times(1)).findById(999L);
        verify(categoryMapper, never()).toDTO(any());
    }

    @Test
    @DisplayName("Should update category successfully")
    void testUpdateCategory_Success() {
        // Arrange
        CategoryRequestDTO updateDTO = new CategoryRequestDTO("Updated Appetizers");

        Category updatedCategory = new Category();
        updatedCategory.setId(1L);
        updatedCategory.setName("Updated Appetizers");

        CategoryResponseDTO updatedResponseDTO = new CategoryResponseDTO(1L, "Updated Appetizers");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);
        when(categoryMapper.toDTO(updatedCategory)).thenReturn(updatedResponseDTO);

        // Act
        CategoryResponseDTO result = categoryService.updateCategory(1L, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Updated Appetizers", result.name());
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(any(Category.class));
        verify(categoryMapper, times(1)).toDTO(updatedCategory);
    }

    @Test
    @DisplayName("Should throw CategoryNotFoundException when updating non-existent category")
    void testUpdateCategory_NotFound() {
        // Arrange
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        CategoryNotFoundException exception = assertThrows(
                CategoryNotFoundException.class,
                () -> categoryService.updateCategory(999L, testRequestDTO)
        );
        assertTrue(exception.getMessage().contains("Category not found with id: 999"));
        verify(categoryRepository, times(1)).findById(999L);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete category successfully")
    void testDeleteCategory_Success() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

        // Act
        categoryService.deleteCategory(1L);

        // Assert
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw CategoryNotFoundException when deleting non-existent category")
    void testDeleteCategory_NotFound() {
        // Arrange
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        CategoryNotFoundException exception = assertThrows(
                CategoryNotFoundException.class,
                () -> categoryService.deleteCategory(999L)
        );
        assertTrue(exception.getMessage().contains("Category not found with id: 999"));
        verify(categoryRepository, times(1)).findById(999L);
        verify(categoryRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Should handle category name update correctly")
    void testUpdateCategory_NameChange() {
        // Arrange
        String newName = "Desserts";
        CategoryRequestDTO updateDTO = new CategoryRequestDTO(newName);

        Category updatedCategory = new Category();
        updatedCategory.setId(1L);
        updatedCategory.setName(newName);

        CategoryResponseDTO updatedResponseDTO = new CategoryResponseDTO(1L, newName);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);
        when(categoryMapper.toDTO(updatedCategory)).thenReturn(updatedResponseDTO);

        // Act
        CategoryResponseDTO result = categoryService.updateCategory(1L, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(newName, result.name());
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

}

