
package com.library.librarymanagementsystem.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.library.librarymanagementsystem.model.PasswordResetRequest;
import com.library.librarymanagementsystem.model.UserModel;
import com.library.librarymanagementsystem.model.UserUpdateDTO;
import com.library.librarymanagementsystem.service.UserService;

/**
 *
 * @author kavya
 */

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "registration";
    }

    @PostMapping("/api/users/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserModel user) {
        try {
            UserModel registeredUser = userService.registerUser(user);
            return ResponseEntity.ok(registeredUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/api/users/login")
    public ResponseEntity<?> loginUser(@RequestBody UserModel loginRequest) {
        try {
            UserModel loggedInUser = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok(loggedInUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/api/users/update")
    public ResponseEntity<?> updateUser(Authentication authentication, @RequestBody UserUpdateDTO userUpdateDTO) {
        try {
            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No authentication found");
            }
            String username = authentication.getName();
            UserModel updatedUser = userService.updateUser(username, userUpdateDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/api/users/reset-password")
    @ResponseBody
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest request) {
        try {
            userService.resetPassword(request.getEmail(), request.getNewPassword());
            return ResponseEntity.ok().body("Password has been reset successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/reset-success")
    public String showResetSuccessPage() {
        return "reset-success";
    }

}
