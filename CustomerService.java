import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerService {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/customer_db";
    private static final String DB_USER = "admin";
    private static final String DB_PASS = "password123";
    
    private Connection connection;
    
    public CustomerService() throws SQLException {
        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }
    
    public void saveCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO customers (customer_id, first_name, last_name, email, phone, credit_limit, is_active) " +
                    "VALUES ('" + customer.getCustomerId() + "', '" + customer.getFirstName() + "', '" + 
                    customer.getLastName() + "', '" + customer.getEmail() + "', '" + customer.getPhoneNumber() + 
                    "', " + customer.getCreditLimit() + ", " + (customer.isActive() ? 1 : 0) + ")";
        
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
    }
    
    public Customer findCustomerById(String customerId) throws SQLException {
        String sql = "SELECT * FROM customers WHERE customer_id = '" + customerId + "'";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        Customer customer = null;
        if (rs.next()) {
            customer = new Customer();
            customer.setCustomerId(rs.getString("customer_id"));
            customer.setFirstName(rs.getString("first_name"));
            customer.setLastName(rs.getString("last_name"));
            customer.setEmail(rs.getString("email"));
            customer.setPhoneNumber(rs.getString("phone"));
            customer.setCreditLimit(rs.getDouble("credit_limit"));
            customer.setActive(rs.getInt("is_active") == 1);
        }
        
        return customer;
    }
    
    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<Customer>();
        String sql = "SELECT * FROM customers";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        while (rs.next()) {
            Customer customer = new Customer();
            customer.setCustomerId(rs.getString("customer_id"));
            customer.setFirstName(rs.getString("first_name"));
            customer.setLastName(rs.getString("last_name"));
            customer.setEmail(rs.getString("email"));
            customer.setPhoneNumber(rs.getString("phone"));
            customer.setCreditLimit(rs.getDouble("credit_limit"));
            customer.setActive(rs.getInt("is_active") == 1);
            customers.add(customer);
        }
        
        return customers;
    }
    
    public void updateCustomerEmail(String customerId, String newEmail) throws SQLException {
        String sql = "UPDATE customers SET email = '" + newEmail + "' WHERE customer_id = '" + customerId + "'";
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
    }
    
    public void deleteCustomer(String customerId) throws SQLException {
        String sql = "DELETE FROM customers WHERE customer_id = '" + customerId + "'";
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
    }
    
    public double calculateTotalCreditLimit() throws SQLException {
        String sql = "SELECT SUM(credit_limit) as total FROM customers WHERE is_active = 1";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        double total = 0;
        if (rs.next()) {
            total = rs.getDouble("total");
        }
        
        return total;
    }
    
    public boolean isValidEmail(String email) {
        if (email == null || email.length() == 0) {
            return false;
        }
        return email.contains("@");
    }
    
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}