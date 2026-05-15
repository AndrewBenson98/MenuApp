package com.benson.menu_app.model.DTO.response;

import java.math.BigDecimal;

public record MenuItemResponseDTO(
         Long id,
         String title,
         String description,
         BigDecimal price
) {
}
