package com.benson.menu_app.service;


import com.benson.menu_app.model.DTO.request.MenuItemRequestDTO;
import com.benson.menu_app.model.DTO.response.MenuItemResponseDTO;
import com.benson.menu_app.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManuItemService implements MenuItemService {

    private final MenuItemRepository menuItemRepository;

    public ManuItemService(@Autowired MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }


    @Override
    public MenuItemResponseDTO createMenuItem(MenuItemRequestDTO menuItemRequestDTO) {
        return null;
    }

    @Override
    public List<MenuItemResponseDTO> getAllMenuItems() {
        return List.of();
    }

    @Override
    public MenuItemResponseDTO getMenuItem(long id) {
        return null;
    }

    @Override
    public MenuItemResponseDTO updateMenuItem(long id, MenuItemRequestDTO menuItemRequestDTO) {
        return null;
    }

    @Override
    public void deleteMenuItem(long id) {

    }
}
