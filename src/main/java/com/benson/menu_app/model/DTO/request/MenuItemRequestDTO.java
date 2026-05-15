package com.benson.menu_app.model.DTO.request;

import java.math.BigDecimal;

public record MenuItemRequestDTO(
         String title,
         String description,
         BigDecimal price
) {
}
