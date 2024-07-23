package com.library.librarymanagementsystem.model;

import lombok.Data;

@Data
public class PasswordResetRequest {
   
       private String email;
        private String newPassword;
     
}
