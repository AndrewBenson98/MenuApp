package com.benson.menu_app.model.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating/updating Category
 */
public record CategoryRequestDTO(
        @NotBlank(message = "name must not be blank")
        @Size(min = 2, max = 50, message = "name must be between 2 and 50 characters")
        String name
) {}
