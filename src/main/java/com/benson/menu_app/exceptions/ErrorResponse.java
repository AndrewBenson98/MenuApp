package com.benson.menu_app.exceptions;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private Integer status;
    private String message;
    private LocalDateTime timestamp;



    public ErrorResponse(String message, Integer status) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();

    }

}
