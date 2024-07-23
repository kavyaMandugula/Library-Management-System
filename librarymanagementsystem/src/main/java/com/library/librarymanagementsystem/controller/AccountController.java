package com.library.librarymanagementsystem.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.library.librarymanagementsystem.entities.User;
import com.library.librarymanagementsystem.entities.User.UserRole;
import com.library.librarymanagementsystem.model.LoanDTO;
import com.library.librarymanagementsystem.service.LoanService;
import com.library.librarymanagementsystem.service.UserService;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String getAccountPage(Model model, Principal principal) {
        if (principal == null) {
            // User is not authenticated, redirect to login page
            return "redirect:/login";
        }
        
        User user = userService.getUserByUsername(principal.getName());
        if (user.getRole() == UserRole.ADMIN) {
            return "redirect:/account/admin";
        } else {
            return "redirect:/account/patron";
        }
    }   

    @GetMapping("/admin")
    public String getAdminAccountPage(Model model) {
        List<LoanDTO> activeLoans = loanService.getAllActiveLoans();
        model.addAttribute("activeLoans", activeLoans);
        return "admin"; 
    }

   
    @GetMapping("/patron")
    public String getPatronAccountPage() {
        return "patron";
    }
   
}
