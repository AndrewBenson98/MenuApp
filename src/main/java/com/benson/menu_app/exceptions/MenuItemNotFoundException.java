package com.benson.menu_app.exceptions;

public class MenuItemNotFoundException   extends RuntimeException
{
    public MenuItemNotFoundException(String message)
    {
        super(message);
    }
}

