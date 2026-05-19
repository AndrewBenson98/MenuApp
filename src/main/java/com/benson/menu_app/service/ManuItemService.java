package com.benson.menu_app.service;


import com.benson.menu_app.model.DTO.request.MenuItemRequestDTO;
import com.benson.menu_app.model.DTO.response.MenuItemResponseDTO;
import com.benson.menu_app.model.MenuItem;
import com.benson.menu_app.repository.MenuItemRepository;
import com.benson.menu_app.utils.MenuItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManuItemService implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final MenuItemMapper menuItemMapper;

    public ManuItemService(@Autowired MenuItemRepository menuItemRepository, @Autowired MenuItemMapper menuItemMapper) {
        this.menuItemRepository = menuItemRepository;
        this.menuItemMapper = menuItemMapper;
    }


    @Override
    public MenuItemResponseDTO createMenuItem(MenuItemRequestDTO menuItemRequestDTO) {
        MenuItem menuItem = menuItemMapper.toEntity(menuItemRequestDTO);
        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        return menuItemMapper.toDto(savedMenuItem);
    }

    @Override
    public List<MenuItemResponseDTO> getAllMenuItems() {

        return menuItemRepository.findAll().stream()
                .map(menuItemMapper::toDto)
                .toList();
    }

    @Override
    public MenuItemResponseDTO getMenuItem(long id) {
        MenuItem menuItem = menuItemRepository.findById(id).orElseThrow(() -> new RuntimeException("Menu item not found with id: " + id));
        return menuItemMapper.toDto(menuItem);
    }

    @Override
    public MenuItemResponseDTO updateMenuItem(long id, MenuItemRequestDTO menuItemRequestDTO) {
        MenuItem menuItem = menuItemRepository.findById(id).orElseThrow(() -> new RuntimeException("Menu item not found with id: " + id));
        menuItem.setTitle(menuItemRequestDTO.title());
        menuItem.setDescription(menuItemRequestDTO.description());
        menuItem.setPrice(menuItemRequestDTO.price());
        MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
        return menuItemMapper.toDto(updatedMenuItem);
    }

    @Override
    public void deleteMenuItem(long id) {
        menuItemRepository.deleteById(id);
    }
}
