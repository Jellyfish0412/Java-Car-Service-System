package customer_function;

public class Testcustomer {
    public static void main(String[] args) {

        CustomerDAO dao = new CustomerDAO();

        System.out.println("");
        System.out.println("=== All Customers ===");
        for (Customer c : dao.getAllCustomers()) {
            System.out.println(c.getCustomerID() + " | " + c.getName());
        }

        System.out.println("\n=== Search by ID ===");
        Customer found = dao.getCustomerByID("C001");
        if (found != null) {
            System.out.println("Found     : " + found.getName());
            System.out.println("Email     : " + found.getEmail());
            System.out.println("Registered: " + found.getRegisteredDate());
        } else {
            System.out.println("Customer not found!");
        }

        System.out.println("\n=== Login Test ===");
        Customer login = dao.getCustomerByLogin("szeying@email.com", "123456");
        String getId = dao.getIdByLogin("szeying@email.com", "123456");
        if (login != null) {
            System.out.println("Login Success! Welcome, " + login.getName());
            System.out.println("Your ID is " + getId);
        } else {
            System.out.println("Login Failed!");
        }
    }
}
