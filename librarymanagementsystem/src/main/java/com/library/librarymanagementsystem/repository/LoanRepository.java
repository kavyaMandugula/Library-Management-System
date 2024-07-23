package com.library.librarymanagementsystem.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.librarymanagementsystem.entities.Loan;
import com.library.librarymanagementsystem.entities.Loan.LoanStatus;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByUserIdAndStatus(Long userId, Loan.LoanStatus status);
    List<Loan> findByStatusAndDueDateBefore(Loan.LoanStatus status, LocalDateTime date);
    List<Loan> findByUserId(Long userId);
    List<Loan> findByStatus(LoanStatus status);
}