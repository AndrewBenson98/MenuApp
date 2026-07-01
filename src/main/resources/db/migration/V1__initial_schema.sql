CREATE TABLE category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE menu_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255),
    description TEXT,
    price DECIMAL(19,2),
    category_id BIGINT,
    FOREIGN KEY (category_id) REFERENCES category(id)
);
