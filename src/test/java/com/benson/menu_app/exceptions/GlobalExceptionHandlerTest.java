package com.benson.menu_app.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("Should handle MenuItemNotFoundException with 404 status")
    void testHandleMenuItemNotFoundException() {
        // Arrange
        String errorMessage = "Menu item not found with id: 1";
        MenuItemNotFoundException exception = new MenuItemNotFoundException(errorMessage);

        // Act
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleMenuItemNotFound(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(errorMessage, response.getBody().getMessage());
        assertEquals(404, response.getBody().getStatus());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    @DisplayName("Should handle CategoryNotFoundException with 404 status")
    void testHandleCategoryNotFoundException() {
        // Arrange
        String errorMessage = "Category not found with id: 1";
        CategoryNotFoundException exception = new CategoryNotFoundException(errorMessage);

        // Act
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleCategoryNotFound(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(errorMessage, response.getBody().getMessage());
        assertEquals(404, response.getBody().getStatus());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    @DisplayName("Should handle CategoryNotFoundException with different id")
    void testHandleCategoryNotFoundException_DifferentId() {
        // Arrange
        String errorMessage = "Category not found with id: 999";
        CategoryNotFoundException exception = new CategoryNotFoundException(errorMessage);

        // Act
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleCategoryNotFound(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("999"));
        assertEquals(404, response.getBody().getStatus());
    }

    @Test
    @DisplayName("Should handle ConstraintViolationException with 400 status")
    void testHandleConstraintViolationException() {
        // Arrange
        Set<ConstraintViolation<?>> violations = new HashSet<>();

        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        violations.add(violation);

        ConstraintViolationException exception = new ConstraintViolationException(
                "Constraint violation",
                violations
        );

        // Act
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleConstraintViolation(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertNotNull(response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    @DisplayName("Should handle Exception with 500 status")
    void testHandleAllExceptions() {
        // Arrange
        Exception exception = new Exception("Internal server error");

        // Act
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleAllExceptions(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Internal server error", response.getBody().getMessage());
        assertEquals(500, response.getBody().getStatus());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    @DisplayName("Should handle generic RuntimeException with 500 status")
    void testHandleRuntimeException() {
        // Arrange
        RuntimeException exception = new RuntimeException("Unexpected runtime error");

        // Act
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleAllExceptions(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getStatus());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    @DisplayName("Should set timestamp in ErrorResponse")
    void testErrorResponseTimestamp() {
        // Arrange
        MenuItemNotFoundException exception = new MenuItemNotFoundException("Test error");

        // Act
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleMenuItemNotFound(exception);

        // Assert
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getTimestamp());
        // Verify that timestamp is set to a recent time
        assertTrue(response.getBody().getTimestamp().isBefore(
                java.time.LocalDateTime.now().plusSeconds(1)
        ));
    }

}

