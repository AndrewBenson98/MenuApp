package com.benson.menu_app.service;

import com.benson.menu_app.exceptions.MenuItemNotFoundException;
import com.benson.menu_app.model.DTO.request.MenuItemRequestDTO;
import com.benson.menu_app.model.DTO.response.MenuItemResponseDTO;

import java.util.List;

public interface MenuItemService {

    MenuItemResponseDTO createMenuItem(MenuItemRequestDTO menuItemRequestDTO);

    List<MenuItemResponseDTO> getAllMenuItems();

    MenuItemResponseDTO getMenuItem(long id) throws MenuItemNotFoundException;

    MenuItemResponseDTO updateMenuItem(long id, MenuItemRequestDTO menuItemRequestDTO) throws MenuItemNotFoundException;

    void deleteMenuItem(long id) throws MenuItemNotFoundException;


}
