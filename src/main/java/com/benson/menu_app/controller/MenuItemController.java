package com.benson.menu_app.controller;

import com.benson.menu_app.model.DTO.request.MenuItemRequestDTO;
import com.benson.menu_app.model.DTO.response.MenuItemResponseDTO;
import com.benson.menu_app.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class MenuItemController {

    @Autowired
    private MenuItemService menuItemService;


    @PostMapping("/menuItems")
    public ResponseEntity<MenuItemResponseDTO> createMenuItem(@RequestBody MenuItemRequestDTO menuItemRequestDTO) {
        MenuItemResponseDTO response = menuItemService.createMenuItem(menuItemRequestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/menuItems")
    public ResponseEntity<List<MenuItemResponseDTO>> getAllMenuItems(){
        List<MenuItemResponseDTO> menuItems = menuItemService.getAllMenuItems();
        return ResponseEntity.ok(menuItems);
    }

    @GetMapping("/menuItems/{id}")
    public ResponseEntity<MenuItemResponseDTO> getMenuItemById(@PathVariable Long id){
        MenuItemResponseDTO menuItem = menuItemService.getMenuItem(id);
        return ResponseEntity.ok(menuItem);
    }

    @PutMapping("/menuItems/{id}")
    public ResponseEntity<MenuItemResponseDTO> updateMenuItem(@PathVariable Long id, @RequestBody MenuItemRequestDTO menuItemDetails){
        MenuItemResponseDTO response = menuItemService.updateMenuItem(id, menuItemDetails);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/menuItems/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id){
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.noContent().build();
    }


}
