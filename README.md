# Library-Management-System

Project Overview
The Library Management System (LMS) is a comprehensive software solution designed to automate and streamline library operations, enhancing the experience for both patrons and staff. This system serves as a digital hub for managing library resources, patron interactions, and administrative tasks efficiently.

#Features

Book Management

Catalog books with details (title, author, ISBN, publication date, genre, etc.)
Track book status (available, checked out, reserved, under maintenance)
Manage multiple copies of the same book
Support for different types of materials (books, journals, audiobooks, DVDs)

User Management

Register new library members
Maintain user profiles with contact information
Track borrowing history and current loans
Manage user categories (students, faculty, public) with different privileges
Circulation

Check-out and check-in processes
Reservation system for books
Automatic fine calculation for overdue items
Renewal of borrowed items
Search and Discovery

Advanced search functionality (by title, author, ISBN, keyword)
Browse by category, genre, or new arrivals
Personalized recommendations based on user history

Administrative Functions

Manage library staff accounts and access levels
Configure system settings (loan periods, fine rates)
Manage library branches (for multi-location systems)

Online Public Access Catalog (OPAC)

Web-based interface for users to search the catalog
User account management (view loans, renew items, pay fines online)
Tech Stack
Frontend: Java Script , HTML, CSS
Backend: Java Spring Boot
Database: MySQL
Authentication: Spring Security 
Testing: JUnit, Mockito
Version Control: Git with GitHub
Installation
Clone the repository
git clone https://github.com/yourusername/library-management-system.git
cd library-management-system

Backend Setup

Ensure Java and Maven are installed on your system.
Navigate to the backend directory.
cd backend
mvn clean install
mvn spring-boot:run


Usage
User Registration and Login

Navigate to the registration page to create a new account.
Use the login page to authenticate with your credentials.
Book Search

Use the search functionality to find books by title, author, or ISBN.
Utilize advanced search for more specific queries.
Book Management

Admin users can add, edit, or remove books from the catalog.
View book details and availability status.
Circulation

Check-out and check-in books.
Place holds on desired materials and renew borrowed items.
User Profile Management

View and update user profile details.
Track borrowing history and current loans.
Admin Functions

Access the admin dashboard to manage users, books, and system settings.
Generate reports and analyze library usage patterns.

Acknowledgements
Special thanks to our instructor for their guidance and support.
