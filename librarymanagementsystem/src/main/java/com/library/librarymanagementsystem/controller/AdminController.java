package com.library.librarymanagementsystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.library.librarymanagementsystem.entities.Loan;
import com.library.librarymanagementsystem.entities.User;
import com.library.librarymanagementsystem.model.LoanDTO;
import com.library.librarymanagementsystem.model.UserModel;
import com.library.librarymanagementsystem.service.BookService;
import com.library.librarymanagementsystem.service.LoanService;
import com.library.librarymanagementsystem.service.UserService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private LoanService loanService;
    @Autowired
    private BookService bookService;

    @GetMapping("/admin")
public String getAdminAccountPage(Model model) {
    return "admin";
}

    @GetMapping("/dashboard")
    public String getAdminDashboard(Model model) {
        List<Loan> activeLoans = loanService.getAllActiveLoansFromLoans();
        List<UserModel> users = userService.getAllUsers();
        model.addAttribute("activeLoans", activeLoans);
        model.addAttribute("users", users);
        return "admin-dashboard";
    }


  @GetMapping("/users/{userId}")
  public ResponseEntity<User> getUserDetails(@PathVariable Long userId) {
      User user = userService.getUserById(userId);
      if (user != null) {
          return ResponseEntity.ok(user);
      } else {
          return ResponseEntity.notFound().build();
      }
  }

  @GetMapping("/users/{userId}/loans")
    public ResponseEntity<List<LoanDTO>> getUserLoans(@PathVariable Long userId) {
        List<LoanDTO> loans = loanService.getLoansByUserId(userId);
        return ResponseEntity.ok(loans);
    }
    @DeleteMapping("/users/{userId}/loans/{loanId}")
    public ResponseEntity<Void> removeBookFromUser(@PathVariable Long userId, @PathVariable Long loanId) {
        loanService.removeLoan(loanId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/search")
    public List<User> searchUsers(@RequestParam String query) {
        return userService.searchUsers(query);
    }
}
