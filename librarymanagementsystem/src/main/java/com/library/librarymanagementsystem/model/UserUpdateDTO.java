package com.library.librarymanagementsystem.model;



import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserUpdateDTO {

    @NotNull
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    
}
