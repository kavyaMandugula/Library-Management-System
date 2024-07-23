package com.library.librarymanagementsystem.model;

import com.library.librarymanagementsystem.entities.Book;

import lombok.Data;

@Data
public class BookDTO {

    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String description;
    private Integer publishedYear;
    private String genre;
    private Double averageRating;
    private Integer numPages;
    private String thumbnail;
    private Integer quantity;
    private Integer availableQuantity;
    private Book.BookStatus status;
    
}
