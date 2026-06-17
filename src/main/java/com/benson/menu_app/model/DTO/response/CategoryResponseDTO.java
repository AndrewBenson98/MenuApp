package com.benson.menu_app.model.DTO.response;

import jakarta.validation.constraints.NotNull;

/**
 * Response DTO for Category
 */
public record CategoryResponseDTO(
        @NotNull(message = "id must not be null")
        long id,

        @NotNull(message = "name must not be null")
        String name
) {
}
