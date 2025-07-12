import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CustomerService - Data Access Layer for Customer Management System
 * 
 * This service class provides CRUD (Create, Read, Update, Delete) operations
 * for Customer entities and handles all database interactions.
 * 
 * Note: This implementation uses direct SQL string concatenation which is
 * vulnerable to SQL injection attacks. In production, use PreparedStatements.
 */
public class CustomerService {
    // Database connection configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/customer_db";
    private static final String DB_USER = "admin";
    private static final String DB_PASS = "password123";
    
    // Single database connection instance for this service
    private Connection connection;
    
    /**
     * Constructor - Establishes database connection
     * @throws SQLException if database connection fails
     */
    public CustomerService() throws SQLException {
        // Initialize MySQL database connection using JDBC
        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }
    
    /**
     * Saves a new customer to the database
     * @param customer Customer object to be saved
     * @throws SQLException if database operation fails
     * WARNING: This method is vulnerable to SQL injection - use PreparedStatement in production
     */
    public void saveCustomer(Customer customer) throws SQLException {
        // Build INSERT SQL statement using string concatenation (vulnerable to SQL injection)
        String sql = "INSERT INTO customers (customer_id, first_name, last_name, email, phone, credit_limit, is_active) " +
                    "VALUES ('" + customer.getCustomerId() + "', '" + customer.getFirstName() + "', '" + 
                    customer.getLastName() + "', '" + customer.getEmail() + "', '" + customer.getPhoneNumber() + 
                    "', " + customer.getCreditLimit() + ", " + (customer.isActive() ? 1 : 0) + ")";
        
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(sql); // Execute the INSERT statement
        stmt.close(); // Always close statements to prevent resource leaks
    }
    
    /**
     * Retrieves a customer by their unique ID
     * @param customerId Unique identifier for the customer
     * @return Customer object if found, null if not found
     * @throws SQLException if database operation fails
     */
    public Customer findCustomerById(String customerId) throws SQLException {
        // Build SELECT query to find customer by ID
        String sql = "SELECT * FROM customers WHERE customer_id = '" + customerId + "'";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        Customer customer = null;
        if (rs.next()) { // Check if a record was found
            // Create new Customer object and populate from database row
            customer = new Customer();
            customer.setCustomerId(rs.getString("customer_id"));
            customer.setFirstName(rs.getString("first_name"));
            customer.setLastName(rs.getString("last_name"));
            customer.setEmail(rs.getString("email"));
            customer.setPhoneNumber(rs.getString("phone"));
            customer.setCreditLimit(rs.getDouble("credit_limit"));
            customer.setActive(rs.getInt("is_active") == 1); // Convert int to boolean
        }
        
        return customer; // Returns null if no customer found
    }
    
    /**
     * Retrieves all customers from the database
     * @return List of all Customer objects in the database
     * @throws SQLException if database operation fails
     */
    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<Customer>();
        String sql = "SELECT * FROM customers"; // Query to get all customer records
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        // Iterate through all rows in the result set
        while (rs.next()) {
            // Create Customer object for each database row
            Customer customer = new Customer();
            customer.setCustomerId(rs.getString("customer_id"));
            customer.setFirstName(rs.getString("first_name"));
            customer.setLastName(rs.getString("last_name"));
            customer.setEmail(rs.getString("email"));
            customer.setPhoneNumber(rs.getString("phone"));
            customer.setCreditLimit(rs.getDouble("credit_limit"));
            customer.setActive(rs.getInt("is_active") == 1); // Convert database int to boolean
            customers.add(customer); // Add to our result list
        }
        
        return customers; // Returns empty list if no customers found
    }
    
    /**
     * Updates a customer's email address
     * @param customerId ID of the customer to update
     * @param newEmail New email address to set
     * @throws SQLException if database operation fails
     */
    public void updateCustomerEmail(String customerId, String newEmail) throws SQLException {
        // Build UPDATE SQL to change customer's email
        String sql = "UPDATE customers SET email = '" + newEmail + "' WHERE customer_id = '" + customerId + "'";
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(sql); // Execute the UPDATE statement
        stmt.close(); // Clean up resources
    }
    
    /**
     * Permanently deletes a customer from the database
     * @param customerId ID of the customer to delete
     * @throws SQLException if database operation fails
     * WARNING: This operation cannot be undone
     */
    public void deleteCustomer(String customerId) throws SQLException {
        // Build DELETE SQL to remove customer permanently
        String sql = "DELETE FROM customers WHERE customer_id = '" + customerId + "'";
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(sql); // Execute the DELETE statement
        stmt.close(); // Clean up resources
    }
    
    /**
     * Calculates the total credit limit for all active customers
     * This is useful for risk assessment and business reporting
     * @return Total credit limit amount for active customers
     * @throws SQLException if database operation fails
     */
    public double calculateTotalCreditLimit() throws SQLException {
        // SQL aggregation query to sum credit limits for active customers only
        String sql = "SELECT SUM(credit_limit) as total FROM customers WHERE is_active = 1";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        double total = 0;
        if (rs.next()) {
            total = rs.getDouble("total"); // Get the calculated sum
        }
        
        return total; // Returns 0 if no active customers found
    }
    
    /**
     * Basic email validation utility method
     * Note: This is a very simple validation - production systems should use
     * more robust email validation (regex patterns, RFC compliance, etc.)
     * @param email Email address to validate
     * @return true if email appears valid, false otherwise
     */
    public boolean isValidEmail(String email) {
        // Check for null or empty string
        if (email == null || email.length() == 0) {
            return false;
        }
        // Simple check for @ symbol (minimal email validation)
        return email.contains("@");
    }
    
    /**
     * Properly closes the database connection to prevent resource leaks
     * This method should be called when the service is no longer needed
     * (e.g., in a try-with-resources block or application shutdown)
     */
    public void close() {
        try {
            if (connection != null) {
                connection.close(); // Close the database connection
            }
        } catch (SQLException e) {
            // Log connection closing errors but don't throw - this is cleanup code
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}