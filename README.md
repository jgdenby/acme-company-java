# Customer Management System

A simple Java-based customer management system with database connectivity and console interface.

## File Descriptions

### Customer.java
**Purpose:** Data model class representing a customer entity
- **Location:** Customer.java:3-118
- **Key Features:**
  - Private fields for customer data (ID, name, email, phone, credit limit, etc.)
  - Default and parameterized constructors
  - Standard getter/setter methods for all fields
  - Business logic methods: `getFullName()`, `getAge()`, `equals()`, `toString()`
  - Default credit limit of $1000 and active status for new customers

### CustomerService.java
**Purpose:** Data access layer providing CRUD operations and business logic
- **Location:** CustomerService.java:5-111
- **Key Features:**
  - Database connection management (MySQL)
  - CRUD operations: save, find, update, delete customers
  - Business methods: `calculateTotalCreditLimit()`, `isValidEmail()`
  - **Security Issue:** Uses string concatenation for SQL queries (vulnerable to SQL injection)
  - Database configuration: localhost:3306/customer_db

### CustomerApp.java
**Purpose:** Main application entry point with console-based user interface
- **Location:** CustomerApp.java:5-187
- **Key Features:**
  - Console menu system with 7 options
  - User input handling and validation
  - Exception handling for database and input errors
  - Coordinates between user interface and CustomerService

## System Architecture & Interactions

```
CustomerApp (UI Layer)
    ↓ uses
CustomerService (Data Access Layer)
    ↓ creates/manipulates
Customer (Data Model)
    ↓ persists to
MySQL Database
```

### Interaction Flow

1. **CustomerApp** serves as the main entry point and user interface
   - Creates a `CustomerService` instance (CustomerApp.java:13)
   - Handles user menu navigation and input collection
   - Calls appropriate `CustomerService` methods based on user choices

2. **CustomerService** acts as the business logic and data access layer
   - Manages database connections and SQL operations
   - Creates and manipulates `Customer` objects
   - Provides validation methods (e.g., email validation)

3. **Customer** represents the data model
   - Used by `CustomerService` to transfer data to/from database
   - Contains business logic for customer-specific operations
   - No direct database dependencies

### Key Dependencies

- **CustomerApp → CustomerService:** Direct dependency for all data operations
- **CustomerService → Customer:** Creates and manipulates Customer objects
- **CustomerService → Database:** JDBC connection to MySQL database
- **All classes:** Standard Java libraries (java.util, java.sql)

### Database Schema Expected
Based on the code, the system expects a MySQL table named `customers` with columns:
- `customer_id` (String)
- `first_name` (String)
- `last_name` (String)
- `email` (String)
- `phone` (String)
- `credit_limit` (Double)
- `is_active` (Integer/Boolean)

## Security Considerations
⚠️ **Warning:** The `CustomerService` class uses string concatenation for SQL queries, making it vulnerable to SQL injection attacks. This should be addressed using PreparedStatements in a production environment.
