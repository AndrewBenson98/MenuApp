package com.benson.menu_app.service;

import com.benson.menu_app.exceptions.CategoryNotFoundException;
import com.benson.menu_app.exceptions.MenuItemNotFoundException;
import com.benson.menu_app.model.DTO.request.MenuItemRequestDTO;
import com.benson.menu_app.model.DTO.response.MenuItemResponseDTO;
import com.benson.menu_app.model.MenuItem;
import com.benson.menu_app.repository.CategoryRepository;
import com.benson.menu_app.repository.MenuItemRepository;
import com.benson.menu_app.utils.MenuItemMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MenuItemService Tests")
class MenuItemServiceImplTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private MenuItemMapper menuItemMapper;

    @Mock
    private CategoryRepository categoryRepository;

    private MenuItemServiceImpl menuItemService;

    private MenuItem testMenuItem;
    private MenuItemRequestDTO testRequestDTO;
    private MenuItemResponseDTO testResponseDTO;

    @BeforeEach
    void setUp() {
        menuItemService = new MenuItemServiceImpl(menuItemRepository, menuItemMapper, categoryRepository);

        // Setup test data
        testMenuItem = new MenuItem();
        testMenuItem.setId(1L);
        testMenuItem.setTitle("Burger");
        testMenuItem.setDescription("Delicious burger");
        testMenuItem.setPrice(new BigDecimal("9.99"));
        testMenuItem.setCategoryId(1L);

        testRequestDTO = new MenuItemRequestDTO(
                "Burger",
                "Delicious burger",
                new BigDecimal("9.99"),
                1L
        );

        testResponseDTO = new MenuItemResponseDTO(
                1L,
                "Burger",
                "Delicious burger",
                new BigDecimal("9.99"),
                1L
        );
    }

    @Test
    @DisplayName("Should create a new menu item successfully")
    void testCreateMenuItem_Success() {
        // Arrange
        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(menuItemMapper.toEntity(testRequestDTO)).thenReturn(testMenuItem);
        when(menuItemRepository.save(testMenuItem)).thenReturn(testMenuItem);
        when(menuItemMapper.toDto(testMenuItem)).thenReturn(testResponseDTO);

        // Act
        MenuItemResponseDTO result = menuItemService.createMenuItem(testRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Burger", result.title());
        assertEquals(new BigDecimal("9.99"), result.price());
        assertEquals(1L, result.categoryId());
        verify(categoryRepository, times(1)).existsById(1L);
        verify(menuItemRepository, times(1)).save(testMenuItem);
        verify(menuItemMapper, times(1)).toEntity(testRequestDTO);
        verify(menuItemMapper, times(1)).toDto(testMenuItem);
    }

    @Test
    @DisplayName("Should throw CategoryNotFoundException when creating menu item with non-existent category")
    void testCreateMenuItem_CategoryNotFound() {
        // Arrange
        when(categoryRepository.existsById(999L)).thenReturn(false);

        MenuItemRequestDTO invalidRequestDTO = new MenuItemRequestDTO(
                "Burger",
                "Delicious burger",
                new BigDecimal("9.99"),
                999L
        );

        // Act & Assert
        CategoryNotFoundException exception = assertThrows(
                CategoryNotFoundException.class,
                () -> menuItemService.createMenuItem(invalidRequestDTO)
        );
        assertTrue(exception.getMessage().contains("Category not found with id: 999"));
        verify(categoryRepository, times(1)).existsById(999L);
        verify(menuItemRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should retrieve all menu items successfully")
    void testGetAllMenuItems_Success() {
        // Arrange
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(testMenuItem);

        MenuItem menuItem2 = new MenuItem();
        menuItem2.setId(2L);
        menuItem2.setTitle("Pizza");
        menuItem2.setDescription("Cheese pizza");
        menuItem2.setPrice(new BigDecimal("12.99"));
        menuItem2.setCategoryId(2L);
        menuItems.add(menuItem2);

        MenuItemResponseDTO responseDTO2 = new MenuItemResponseDTO(
                2L,
                "Pizza",
                "Cheese pizza",
                new BigDecimal("12.99"),
                2L
        );

        when(menuItemRepository.findAll()).thenReturn(menuItems);
        when(menuItemMapper.toDto(testMenuItem)).thenReturn(testResponseDTO);
        when(menuItemMapper.toDto(menuItem2)).thenReturn(responseDTO2);

        // Act
        List<MenuItemResponseDTO> result = menuItemService.getAllMenuItems();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Burger", result.get(0).title());
        assertEquals("Pizza", result.get(1).title());
        assertEquals(1L, result.get(0).categoryId());
        assertEquals(2L, result.get(1).categoryId());
        verify(menuItemRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no menu items exist")
    void testGetAllMenuItems_Empty() {
        // Arrange
        when(menuItemRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<MenuItemResponseDTO> result = menuItemService.getAllMenuItems();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(menuItemRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should retrieve menu item by id successfully")
    void testGetMenuItem_Success() {
        // Arrange
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(testMenuItem));
        when(menuItemMapper.toDto(testMenuItem)).thenReturn(testResponseDTO);

        // Act
        MenuItemResponseDTO result = menuItemService.getMenuItem(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Burger", result.title());
        assertEquals(1L, result.categoryId());
        verify(menuItemRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw MenuItemNotFoundException when menu item does not exist")
    void testGetMenuItem_NotFound() {
        // Arrange
        when(menuItemRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        MenuItemNotFoundException exception = assertThrows(
                MenuItemNotFoundException.class,
                () -> menuItemService.getMenuItem(999L)
        );
        assertTrue(exception.getMessage().contains("Menu item not found with id: 999"));
        verify(menuItemRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should update menu item successfully")
    void testUpdateMenuItem_Success() {
        // Arrange
        MenuItemRequestDTO updateDTO = new MenuItemRequestDTO(
                "Updated Burger",
                "Updated description",
                new BigDecimal("11.99"),
                1L
        );

        MenuItem updatedMenuItem = new MenuItem();
        updatedMenuItem.setId(1L);
        updatedMenuItem.setTitle("Updated Burger");
        updatedMenuItem.setDescription("Updated description");
        updatedMenuItem.setPrice(new BigDecimal("11.99"));
        updatedMenuItem.setCategoryId(1L);

        MenuItemResponseDTO updatedResponseDTO = new MenuItemResponseDTO(
                1L,
                "Updated Burger",
                "Updated description",
                new BigDecimal("11.99"),
                1L
        );

        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(testMenuItem));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(updatedMenuItem);
        when(menuItemMapper.toDto(updatedMenuItem)).thenReturn(updatedResponseDTO);

        // Act
        MenuItemResponseDTO result = menuItemService.updateMenuItem(1L, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Burger", result.title());
        assertEquals("Updated description", result.description());
        assertEquals(new BigDecimal("11.99"), result.price());
        assertEquals(1L, result.categoryId());
        verify(categoryRepository, times(1)).existsById(1L);
        verify(menuItemRepository, times(1)).findById(1L);
        verify(menuItemRepository, times(1)).save(any(MenuItem.class));
    }

    @Test
    @DisplayName("Should update menu item to a different category successfully")
    void testUpdateMenuItem_DifferentCategory() {
        // Arrange
        MenuItemRequestDTO updateDTO = new MenuItemRequestDTO(
                "Updated Burger",
                "Updated description",
                new BigDecimal("11.99"),
                2L
        );

        MenuItem updatedMenuItem = new MenuItem();
        updatedMenuItem.setId(1L);
        updatedMenuItem.setTitle("Updated Burger");
        updatedMenuItem.setDescription("Updated description");
        updatedMenuItem.setPrice(new BigDecimal("11.99"));
        updatedMenuItem.setCategoryId(2L);

        MenuItemResponseDTO updatedResponseDTO = new MenuItemResponseDTO(
                1L,
                "Updated Burger",
                "Updated description",
                new BigDecimal("11.99"),
                2L
        );

        when(categoryRepository.existsById(2L)).thenReturn(true);
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(testMenuItem));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(updatedMenuItem);
        when(menuItemMapper.toDto(updatedMenuItem)).thenReturn(updatedResponseDTO);

        // Act
        MenuItemResponseDTO result = menuItemService.updateMenuItem(1L, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(2L, result.categoryId());
        verify(categoryRepository, times(1)).existsById(2L);
        verify(menuItemRepository, times(1)).findById(1L);
        verify(menuItemRepository, times(1)).save(any(MenuItem.class));
    }

    @Test
    @DisplayName("Should throw CategoryNotFoundException when updating with non-existent category")
    void testUpdateMenuItem_CategoryNotFound() {
        // Arrange
        MenuItemRequestDTO updateDTO = new MenuItemRequestDTO(
                "Updated Burger",
                "Updated description",
                new BigDecimal("11.99"),
                999L
        );

        when(categoryRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        CategoryNotFoundException exception = assertThrows(
                CategoryNotFoundException.class,
                () -> menuItemService.updateMenuItem(1L, updateDTO)
        );
        assertTrue(exception.getMessage().contains("Category not found with id: 999"));
        verify(categoryRepository, times(1)).existsById(999L);
        verify(menuItemRepository, never()).findById(any());
        verify(menuItemRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw MenuItemNotFoundException when updating non-existent menu item")
    void testUpdateMenuItem_NotFound() {
        // Arrange
        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(menuItemRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        MenuItemNotFoundException exception = assertThrows(
                MenuItemNotFoundException.class,
                () -> menuItemService.updateMenuItem(999L, testRequestDTO)
        );
        assertTrue(exception.getMessage().contains("Menu item not found with id: 999"));
        verify(categoryRepository, times(1)).existsById(1L);
        verify(menuItemRepository, times(1)).findById(999L);
        verify(menuItemRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete menu item successfully")
    void testDeleteMenuItem_Success() {
        // Arrange
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(testMenuItem));

        // Act
        menuItemService.deleteMenuItem(1L);

        // Assert
        verify(menuItemRepository, times(1)).findById(1L);
        verify(menuItemRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw MenuItemNotFoundException when deleting non-existent menu item")
    void testDeleteMenuItem_NotFound() {
        // Arrange
        when(menuItemRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        MenuItemNotFoundException exception = assertThrows(
                MenuItemNotFoundException.class,
                () -> menuItemService.deleteMenuItem(999L)
        );
        assertTrue(exception.getMessage().contains("Menu item not found with id: 999"));
        verify(menuItemRepository, times(1)).findById(999L);
        verify(menuItemRepository, never()).deleteById(any());
    }

}

