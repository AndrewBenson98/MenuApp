package com.benson.menu_app.controller;

import com.benson.menu_app.TestConfig;
import com.benson.menu_app.exceptions.CategoryNotFoundException;
import com.benson.menu_app.model.DTO.request.CategoryRequestDTO;
import com.benson.menu_app.model.DTO.response.CategoryResponseDTO;
import com.benson.menu_app.service.CategoryServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
@Import(TestConfig.class)
@DisplayName("CategoryController Tests")
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CategoryServiceImpl categoryService;

    private CategoryRequestDTO testRequestDTO;
    private CategoryResponseDTO testResponseDTO;

    @BeforeEach
    void setUp() {
        testRequestDTO = new CategoryRequestDTO("Appetizers");
        testResponseDTO = new CategoryResponseDTO(1L, "Appetizers");
    }

    @Test
    @DisplayName("Should create a category successfully")
    void testCreateCategory_Success() throws Exception {
        // Arrange
        when(categoryService.createCategory(any(CategoryRequestDTO.class)))
                .thenReturn(testResponseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Appetizers")));

        verify(categoryService, times(1)).createCategory(any(CategoryRequestDTO.class));
    }

    @Test
    @DisplayName("Should return 400 when creating category with blank name")
    void testCreateCategory_BlankName() throws Exception {
        // Arrange
        CategoryRequestDTO invalidRequestDTO = new CategoryRequestDTO("");

        // Act & Assert
        mockMvc.perform(post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequestDTO)))
                .andExpect(status().isBadRequest());

        verify(categoryService, never()).createCategory(any());
    }

    @Test
    @DisplayName("Should return 400 when creating category with null name")
    void testCreateCategory_NullName() throws Exception {
        // Arrange
        String invalidJson = "{}";

        // Act & Assert
        mockMvc.perform(post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());

        verify(categoryService, never()).createCategory(any());
    }

    @Test
    @DisplayName("Should retrieve all categories successfully")
    void testGetAllCategories_Success() throws Exception {
        // Arrange
        CategoryResponseDTO responseDTO2 = new CategoryResponseDTO(2L, "Main Courses");
        List<CategoryResponseDTO> categories = Arrays.asList(testResponseDTO, responseDTO2);

        when(categoryService.getAllCategories()).thenReturn(categories);

        // Act & Assert
        mockMvc.perform(get("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Appetizers")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Main Courses")));

        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    @DisplayName("Should return empty list when no categories exist")
    void testGetAllCategories_Empty() throws Exception {
        // Arrange
        when(categoryService.getAllCategories()).thenReturn(new ArrayList<>());

        // Act & Assert
        mockMvc.perform(get("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    @DisplayName("Should retrieve category by id successfully")
    void testGetCategoryById_Success() throws Exception {
        // Arrange
        when(categoryService.getCategory(1L)).thenReturn(testResponseDTO);

        // Act & Assert
        mockMvc.perform(get("/api/v1/categories/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Appetizers")));

        verify(categoryService, times(1)).getCategory(1L);
    }

    @Test
    @DisplayName("Should return 404 when category not found")
    void testGetCategoryById_NotFound() throws Exception {
        // Arrange
        when(categoryService.getCategory(999L))
                .thenThrow(new CategoryNotFoundException("Category not found with id: 999"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/categories/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("Category not found")));

        verify(categoryService, times(1)).getCategory(999L);
    }

    @Test
    @DisplayName("Should update category successfully")
    void testUpdateCategory_Success() throws Exception {
        // Arrange
        CategoryRequestDTO updateDTO = new CategoryRequestDTO("Updated Appetizers");
        CategoryResponseDTO updatedResponseDTO = new CategoryResponseDTO(1L, "Updated Appetizers");

        when(categoryService.updateCategory(eq(1L), any(CategoryRequestDTO.class)))
                .thenReturn(updatedResponseDTO);

        // Act & Assert
        mockMvc.perform(put("/api/v1/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Updated Appetizers")));

        verify(categoryService, times(1)).updateCategory(eq(1L), any(CategoryRequestDTO.class));
    }

    @Test
    @DisplayName("Should return 400 when updating category with invalid name")
    void testUpdateCategory_InvalidName() throws Exception {
        // Arrange
        CategoryRequestDTO updateDTO = new CategoryRequestDTO("A");  // Too short

        // Act & Assert
        mockMvc.perform(put("/api/v1/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isBadRequest());

        verify(categoryService, never()).updateCategory(anyLong(), any());
    }

    @Test
    @DisplayName("Should return 404 when updating non-existent category")
    void testUpdateCategory_NotFound() throws Exception {
        // Arrange
        when(categoryService.updateCategory(eq(999L), any(CategoryRequestDTO.class)))
                .thenThrow(new CategoryNotFoundException("Category not found with id: 999"));

        // Act & Assert
        mockMvc.perform(put("/api/v1/categories/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRequestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("Category not found")));

        verify(categoryService, times(1)).updateCategory(eq(999L), any(CategoryRequestDTO.class));
    }

    @Test
    @DisplayName("Should delete category successfully")
    void testDeleteCategory_Success() throws Exception {
        // Arrange
        doNothing().when(categoryService).deleteCategory(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/categories/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(categoryService, times(1)).deleteCategory(1L);
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existent category")
    void testDeleteCategory_NotFound() throws Exception {
        // Arrange
        doThrow(new CategoryNotFoundException("Category not found with id: 999"))
                .when(categoryService).deleteCategory(999L);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/categories/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("Category not found")));

        verify(categoryService, times(1)).deleteCategory(999L);
    }

    @Test
    @DisplayName("Should return 201 with location header on successful creation")
    void testCreateCategory_LocationHeader() throws Exception {
        // Arrange
        when(categoryService.createCategory(any(CategoryRequestDTO.class)))
                .thenReturn(testResponseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/v1/categories/1")));

        verify(categoryService, times(1)).createCategory(any(CategoryRequestDTO.class));
    }

}

