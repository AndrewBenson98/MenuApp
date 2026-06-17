package com.benson.menu_app.model.DTO.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record MenuItemRequestDTO(
        @NotNull(message = "title must not be null")
        @Size(min = 2, max = 100, message = "title must be between 2 and 100 characters")
        String title,

        @NotNull(message = "description must not be null")
        @Size(min = 5, max = 500, message = "description must be between 5 and 500 characters")
        String description,

        @NotNull(message = "price must not be null")
        @Positive(message = "price must be greater than zero")
        @Digits(integer = 10, fraction = 2, message = "price must have at most 10 digits and 2 decimal places")
        BigDecimal price
) {
}
