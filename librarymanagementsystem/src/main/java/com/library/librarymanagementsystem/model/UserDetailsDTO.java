package com.library.librarymanagementsystem.model;

import java.util.List;

import com.library.librarymanagementsystem.entities.User;

import lombok.Data;

@Data
public class UserDetailsDTO {

    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private User.UserRole role;
    private User.AccountStatus status;
    private List<LoanDTO> loans;
}


