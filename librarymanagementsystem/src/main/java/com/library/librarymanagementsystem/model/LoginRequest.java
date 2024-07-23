package com.library.librarymanagementsystem.model;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}