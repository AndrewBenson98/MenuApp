package com.benson.menu_app.controller;

import com.benson.menu_app.model.MenuItem;
import com.benson.menu_app.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class MenuItemController {

    @Autowired
    private MenuItemRepository menuItemRepository;


    @PostMapping("/menuItems")
    public ResponseEntity<MenuItem> createMenuItem(@RequestBody MenuItem menuItem){

        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        return ResponseEntity.ok(savedMenuItem);

    }

    @GetMapping("/menuItems")
    public ResponseEntity<List<MenuItem>> getAllMenuItems(){
        List<MenuItem> menuItems = menuItemRepository.findAll();
        return ResponseEntity.ok(menuItems);
    }

    @GetMapping("/menuItems/{id}")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable Long id){
        MenuItem menuItem = menuItemRepository.findById(id).orElse(null);
        if(menuItem == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(menuItem);
    }

    @PutMapping("/menuItems/{id}")
    public ResponseEntity<MenuItem> updateMenuItem(@PathVariable Long id, @RequestBody MenuItem menuItemDetails){
        MenuItem menuItem = menuItemRepository.findById(id).orElse(null);
        if(menuItem == null){
            return ResponseEntity.notFound().build();
        }
        menuItem.setTitle(menuItemDetails.getTitle());
        menuItem.setDescription(menuItemDetails.getDescription());
        menuItem.setPrice(menuItemDetails.getPrice());
        MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
        return ResponseEntity.ok(updatedMenuItem);
    }

    @DeleteMapping("/menuItems/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id){
        MenuItem menuItem = menuItemRepository.findById(id).orElse(null);
        if(menuItem == null){
            return ResponseEntity.notFound().build();
        }
        menuItemRepository.delete(menuItem);
        return ResponseEntity.noContent().build();
    }


}
