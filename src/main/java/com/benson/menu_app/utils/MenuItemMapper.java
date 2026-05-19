package com.benson.menu_app.utils;

import com.benson.menu_app.model.DTO.request.MenuItemRequestDTO;
import com.benson.menu_app.model.DTO.response.MenuItemResponseDTO;
import com.benson.menu_app.model.MenuItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MenuItemMapper {

    MenuItem toEntity(MenuItemRequestDTO item);
    MenuItemResponseDTO toDto(MenuItem item);


}
