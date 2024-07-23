package com.library.librarymanagementsystem.model;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LoanModel {
    

    private Long id;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Book ID is required")
    private Long bookId;

    @NotNull(message = "Checkout date is required")
    private LocalDateTime checkoutDate;

    @NotNull(message = "Due date is required")
    private LocalDateTime dueDate;

    private LocalDateTime returnDate;

    @NotNull(message = "Status is required")
    private LoanStatus status;

    public enum LoanStatus {
        ACTIVE, RETURNED, OVERDUE
    }
}
