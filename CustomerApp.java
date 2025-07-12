import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class CustomerApp {
    private static CustomerService customerService;
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("=== Customer Management System ===");
        
        try {
            customerService = new CustomerService();
            
            while (true) {
                showMenu();
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                switch (choice) {
                    case 1:
                        addNewCustomer();
                        break;
                    case 2:
                        findCustomer();
                        break;
                    case 3:
                        listAllCustomers();
                        break;
                    case 4:
                        updateCustomerEmail();
                        break;
                    case 5:
                        deleteCustomer();
                        break;
                    case 6:
                        showTotalCreditLimit();
                        break;
                    case 7:
                        System.out.println("Goodbye!");
                        customerService.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
    
    private static void showMenu() {
        System.out.println("\n--- Menu ---");
        System.out.println("1. Add New Customer");
        System.out.println("2. Find Customer");
        System.out.println("3. List All Customers");
        System.out.println("4. Update Customer Email");
        System.out.println("5. Delete Customer");
        System.out.println("6. Show Total Credit Limit");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
    }
    
    private static void addNewCustomer() {
        try {
            System.out.print("Enter Customer ID: ");
            String id = scanner.nextLine();
            
            System.out.print("Enter First Name: ");
            String firstName = scanner.nextLine();
            
            System.out.print("Enter Last Name: ");
            String lastName = scanner.nextLine();
            
            System.out.print("Enter Email: ");
            String email = scanner.nextLine();
            
            if (!customerService.isValidEmail(email)) {
                System.out.println("Invalid email format!");
                return;
            }
            
            Customer customer = new Customer(id, firstName, lastName, email);
            
            System.out.print("Enter Phone Number (optional): ");
            String phone = scanner.nextLine();
            if (phone.length() > 0) {
                customer.setPhoneNumber(phone);
            }
            
            System.out.print("Enter Credit Limit (default 1000): ");
            String creditInput = scanner.nextLine();
            if (creditInput.length() > 0) {
                double creditLimit = Double.parseDouble(creditInput);
                customer.setCreditLimit(creditLimit);
            }
            
            customerService.saveCustomer(customer);
            System.out.println("Customer added successfully!");
            
        } catch (SQLException e) {
            System.out.println("Error adding customer: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format for credit limit!");
        }
    }
    
    private static void findCustomer() {
        try {
            System.out.print("Enter Customer ID: ");
            String id = scanner.nextLine();
            
            Customer customer = customerService.findCustomerById(id);
            if (customer != null) {
                System.out.println("Customer found:");
                System.out.println(customer.toString());
                System.out.println("Age: " + customer.getAge());
            } else {
                System.out.println("Customer not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error finding customer: " + e.getMessage());
        }
    }
    
    private static void listAllCustomers() {
        try {
            List<Customer> customers = customerService.getAllCustomers();
            if (customers.size() == 0) {
                System.out.println("No customers found.");
            } else {
                System.out.println("All customers:");
                for (int i = 0; i < customers.size(); i++) {
                    Customer customer = customers.get(i);
                    System.out.println((i + 1) + ". " + customer.toString());
                }
            }
        } catch (SQLException e) {
            System.out.println("Error listing customers: " + e.getMessage());
        }
    }
    
    private static void updateCustomerEmail() {
        try {
            System.out.print("Enter Customer ID: ");
            String id = scanner.nextLine();
            
            System.out.print("Enter New Email: ");
            String newEmail = scanner.nextLine();
            
            if (!customerService.isValidEmail(newEmail)) {
                System.out.println("Invalid email format!");
                return;
            }
            
            customerService.updateCustomerEmail(id, newEmail);
            System.out.println("Email updated successfully!");
            
        } catch (SQLException e) {
            System.out.println("Error updating email: " + e.getMessage());
        }
    }
    
    private static void deleteCustomer() {
        try {
            System.out.print("Enter Customer ID to delete: ");
            String id = scanner.nextLine();
            
            customerService.deleteCustomer(id);
            System.out.println("Customer deleted successfully!");
            
        } catch (SQLException e) {
            System.out.println("Error deleting customer: " + e.getMessage());
        }
    }
    
    private static void showTotalCreditLimit() {
        try {
            double total = customerService.calculateTotalCreditLimit();
            System.out.println("Total Credit Limit for Active Customers: $" + total);
        } catch (SQLException e) {
            System.out.println("Error calculating total: " + e.getMessage());
        }
    }
}