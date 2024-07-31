# Library Management System (LMS)

## Project Overview
The Library Management System (LMS) is a comprehensive software solution designed to automate and streamline library operations, enhancing the experience for both patrons and staff. This system serves as a digital hub for managing library resources, patron interactions, and administrative tasks efficiently.

## Features

### Book Management
- Catalog books with details (title, author, ISBN, publication date, genre, etc.)
- Track book status (available, checked out, reserved, under maintenance)
- Manage multiple copies of the same book
- Support for different types of materials (books, journals, audiobooks, DVDs)

### User Management
- Register new library members
- Maintain user profiles with contact information
- Track borrowing history and current loans
- Manage user categories (students, faculty, public) with different privileges

### Circulation
- Check-out and check-in processes
- Reservation system for books
- Automatic fine calculation for overdue items
- Renewal of borrowed items

### Search and Discovery
- Advanced search functionality (by title, author, ISBN, keyword)
- Browse by category, genre, or new arrivals
- Personalized recommendations based on user history

### Administrative Functions
- Manage library staff accounts and access levels
- Configure system settings (loan periods, fine rates)
- Manage library branches (for multi-location systems)

### Online Public Access Catalog (OPAC)
- Web-based interface for users to search the catalog
- User account management (view loans, renew items, pay fines online)

## Tech Stack
- **Frontend**: JavaScript, HTML, CSS
- **Backend**: Java Spring Boot
- **Database**: MySQL
- **Authentication**: Spring Security
- **Testing**: JUnit, Mockito
- **Version Control**: Git with GitHub

## Installation

### Clone the repository
```bash
git clone https://github.com/yourusername/library-management-system.git
cd library-management-system

###Backend Setup
Ensure Java and Maven are installed on your system.
Navigate to the backend directory.

```bash
cd backend
mvn clean install
mvn spring-boot:run

