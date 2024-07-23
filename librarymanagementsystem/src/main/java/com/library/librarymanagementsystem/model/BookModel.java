package com.library.librarymanagementsystem.model;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.library.librarymanagementsystem.entities.Book;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class BookModel {

    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    @Pattern(regexp = "^(?:[0-9]{13})?$", message = "ISBN must be 13 digits")
    private String isbn;

    @Size(max = 355, message = "Description must be at most 355 characters")
    private String description;

    @Min(value = 1000, message = "Published year must be 1000 or later")
    @Max(value = 9999, message = "Published year must be 9999 or earlier")
    private Integer publishedYear;

    private String genre;

    @DecimalMin(value = "0.0", message = "Average rating must be at least 0")
    @DecimalMax(value = "5.0", message = "Average rating must be at most 5")
    private Float averageRating;

    @Positive(message = "Number of pages must be positive")
    private Integer numPages;

    private String thumbnail;

    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @Min(value = 0, message = "Available quantity cannot be negative")
    private Integer availableQuantity;

    @NotNull(message = "Status is required")
    private Book.BookStatus status;
    
}
