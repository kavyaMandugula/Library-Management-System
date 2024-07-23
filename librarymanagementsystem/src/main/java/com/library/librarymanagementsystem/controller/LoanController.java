package com.library.librarymanagementsystem.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.librarymanagementsystem.entities.User;
import com.library.librarymanagementsystem.model.LoanDTO;
import com.library.librarymanagementsystem.service.LoanService;
import com.library.librarymanagementsystem.service.UserService;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private UserService userService;

    @Autowired
    private LoanService loanService;

    @GetMapping("/loans")
    public ResponseEntity<List<LoanDTO>> getUserLoans(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = userService.getUserByUsername(principal.getName());
        List<LoanDTO> userLoans = loanService.getLoansByUserId(user.getId());
        return ResponseEntity.ok(userLoans);
    }

}
