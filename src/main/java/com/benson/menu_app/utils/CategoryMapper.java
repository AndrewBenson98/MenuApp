package com.benson.menu_app.utils;

import com.benson.menu_app.model.Category;
import com.benson.menu_app.model.DTO.response.CategoryResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(String name);
    CategoryResponseDTO toDTO(Category category);
}
