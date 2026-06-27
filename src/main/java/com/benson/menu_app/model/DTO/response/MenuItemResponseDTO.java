package com.benson.menu_app.model.DTO.response;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MenuItemResponseDTO(
        @NotNull(message = "id must not be null")
        Long id,

        @NotNull(message = "title must not be null")
        String title,

        @NotNull(message = "description must not be null")
        String description,

        @NotNull(message = "price must not be null")
        BigDecimal price,

        @NotNull(message = "categoryId must not be null")
        Long categoryId
) {
}
