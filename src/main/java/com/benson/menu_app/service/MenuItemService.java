package com.benson.menu_app.service;

import com.benson.menu_app.model.DTO.request.MenuItemRequestDTO;
import com.benson.menu_app.model.DTO.response.MenuItemResponseDTO;

import java.util.List;

public interface MenuItemService {

    MenuItemResponseDTO createMenuItem(MenuItemRequestDTO menuItemRequestDTO);

    List<MenuItemResponseDTO> getAllMenuItems();

    MenuItemResponseDTO getMenuItem(long id);

    MenuItemResponseDTO updateMenuItem(long id, MenuItemRequestDTO menuItemRequestDTO);

    void deleteMenuItem(long id);


}
