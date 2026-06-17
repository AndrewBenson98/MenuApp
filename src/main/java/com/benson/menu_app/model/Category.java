package com.benson.menu_app.model;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Category {

    private long id;
    private String name;

}
