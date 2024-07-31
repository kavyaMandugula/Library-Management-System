package com.library.librarymanagementsystem.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanDTO {

    private Long id;
    private String bookTitle;
    private String bookAuthor;
    private LocalDateTime checkoutDate;
    private LocalDateTime dueDate;
    private String status;
    
}
