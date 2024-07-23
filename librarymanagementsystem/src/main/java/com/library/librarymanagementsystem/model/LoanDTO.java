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
    @Override
    public String toString() {
        return "LoanDTO [id=" + id + ", bookTitle=" + bookTitle + ", bookAuthor=" + bookAuthor + ", checkoutDate="
                + checkoutDate + ", dueDate=" + dueDate + ", status=" + status + "]";
    }
    private String bookTitle;
    private String bookAuthor;
    private LocalDateTime checkoutDate;
    private LocalDateTime dueDate;
    private String status;
    
}
