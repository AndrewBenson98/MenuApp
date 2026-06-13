package com.benson.menu_app.controller;

import com.benson.menu_app.TestConfig;
import com.benson.menu_app.exceptions.MenuItemNotFoundException;
import com.benson.menu_app.model.DTO.request.MenuItemRequestDTO;
import com.benson.menu_app.model.DTO.response.MenuItemResponseDTO;
import com.benson.menu_app.service.MenuItemServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;



import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MenuItemController.class)
@Import(TestConfig.class)
@DisplayName("MenuItemController Tests")
class MenuItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MenuItemServiceImpl menuItemService;

    private MenuItemRequestDTO testRequestDTO;
    private MenuItemResponseDTO testResponseDTO;

    @BeforeEach
    void setUp() {
        testRequestDTO = new MenuItemRequestDTO(
                "Burger",
                "Delicious burger",
                new BigDecimal("9.99")
        );

        testResponseDTO = new MenuItemResponseDTO(
                1L,
                "Burger",
                "Delicious burger",
                new BigDecimal("9.99")
        );
    }

    @Test
    @DisplayName("Should create a menu item successfully")
    void testCreateMenuItem_Success() throws Exception {
        // Arrange
        when(menuItemService.createMenuItem(any(MenuItemRequestDTO.class)))
                .thenReturn(testResponseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/v1/menuItems")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Burger")))
                .andExpect(jsonPath("$.description", is("Delicious burger")))
                .andExpect(jsonPath("$.price", is(9.99)));

        verify(menuItemService, times(1)).createMenuItem(any(MenuItemRequestDTO.class));
    }

    @Test
    @DisplayName("Should retrieve all menu items successfully")
    void testGetAllMenuItems_Success() throws Exception {
        // Arrange
        MenuItemResponseDTO responseDTO2 = new MenuItemResponseDTO(
                2L,
                "Pizza",
                "Cheese pizza",
                new BigDecimal("12.99")
        );
        List<MenuItemResponseDTO> menuItems = Arrays.asList(testResponseDTO, responseDTO2);

        when(menuItemService.getAllMenuItems()).thenReturn(menuItems);

        // Act & Assert
        mockMvc.perform(get("/api/v1/menuItems")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Burger")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("Pizza")));

        verify(menuItemService, times(1)).getAllMenuItems();
    }

    @Test
    @DisplayName("Should return empty list when no menu items exist")
    void testGetAllMenuItems_Empty() throws Exception {
        // Arrange
        when(menuItemService.getAllMenuItems()).thenReturn(new ArrayList<>());

        // Act & Assert
        mockMvc.perform(get("/api/v1/menuItems")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(menuItemService, times(1)).getAllMenuItems();
    }

    @Test
    @DisplayName("Should retrieve menu item by id successfully")
    void testGetMenuItemById_Success() throws Exception {
        // Arrange
        when(menuItemService.getMenuItem(1L)).thenReturn(testResponseDTO);

        // Act & Assert
        mockMvc.perform(get("/api/v1/menuItems/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Burger")))
                .andExpect(jsonPath("$.description", is("Delicious burger")))
                .andExpect(jsonPath("$.price", is(9.99)));

        verify(menuItemService, times(1)).getMenuItem(1L);
    }

    @Test
    @DisplayName("Should return 404 when menu item not found")
    void testGetMenuItemById_NotFound() throws Exception {
        // Arrange
        when(menuItemService.getMenuItem(999L))
                .thenThrow(new MenuItemNotFoundException("Menu item not found with id: 999"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/menuItems/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("Menu item not found")));

        verify(menuItemService, times(1)).getMenuItem(999L);
    }

    @Test
    @DisplayName("Should update menu item successfully")
    void testUpdateMenuItem_Success() throws Exception {
        // Arrange
        MenuItemRequestDTO updateDTO = new MenuItemRequestDTO(
                "Updated Burger",
                "Updated description",
                new BigDecimal("11.99")
        );

        MenuItemResponseDTO updatedResponseDTO = new MenuItemResponseDTO(
                1L,
                "Updated Burger",
                "Updated description",
                new BigDecimal("11.99")
        );

        when(menuItemService.updateMenuItem(eq(1L), any(MenuItemRequestDTO.class)))
                .thenReturn(updatedResponseDTO);

        // Act & Assert
        mockMvc.perform(put("/api/v1/menuItems/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Updated Burger")))
                .andExpect(jsonPath("$.description", is("Updated description")))
                .andExpect(jsonPath("$.price", is(11.99)));

        verify(menuItemService, times(1)).updateMenuItem(eq(1L), any(MenuItemRequestDTO.class));
    }

    @Test
    @DisplayName("Should return 404 when updating non-existent menu item")
    void testUpdateMenuItem_NotFound() throws Exception {
        // Arrange
        when(menuItemService.updateMenuItem(eq(999L), any(MenuItemRequestDTO.class)))
                .thenThrow(new MenuItemNotFoundException("Menu item not found with id: 999"));

        // Act & Assert
        mockMvc.perform(put("/api/v1/menuItems/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRequestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("Menu item not found")));

        verify(menuItemService, times(1)).updateMenuItem(eq(999L), any(MenuItemRequestDTO.class));
    }

    @Test
    @DisplayName("Should delete menu item successfully")
    void testDeleteMenuItem_Success() throws Exception {
        // Arrange
        doNothing().when(menuItemService).deleteMenuItem(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/menuItems/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(menuItemService, times(1)).deleteMenuItem(1L);
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existent menu item")
    void testDeleteMenuItem_NotFound() throws Exception {
        // Arrange
        doThrow(new MenuItemNotFoundException("Menu item not found with id: 999"))
                .when(menuItemService).deleteMenuItem(999L);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/menuItems/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("Menu item not found")));

        verify(menuItemService, times(1)).deleteMenuItem(999L);
    }

}

