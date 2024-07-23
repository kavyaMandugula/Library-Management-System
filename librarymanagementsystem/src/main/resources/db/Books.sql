CREATE TABLE books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(13) UNIQUE,
    description TEXT, 
    published_year YEAR,
    genre VARCHAR(100),
    average_rating FLOAT,
    quantity INT NOT NULL DEFAULT 1,
    available_quantity INT NOT NULL DEFAULT 1,
    num_pages INT,
    thumbnail VARCHAR(255),
    status ENUM('AVAILABLE', 'CHECKED_OUT', 'RESERVED', 'UNDER_MAINTENANCE') NOT NULL DEFAULT 'AVAILABLE'
);