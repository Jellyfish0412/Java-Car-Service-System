package customer_function;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private static final String FILE_PATH = "/Users/jellyfish/Desktop/AI Y2S1/OOP/JAVA ASSIGNMENT/src/customer_data/customer.txt";
    
    public List<Customer> getAllCustomers() {
        List<Customer> list = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(FILE_PATH));
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 7){
                    Customer c = new Customer(
                        parts[0], 
                        parts[1], 
                        parts[2], 
                        parts[3], 
                        parts[4], 
                        parts[5],
                        parts[6]
                    );
                    list.add(c);
                }
            }
            br.close();

        } catch (IOException e) {
            System.err.println("Error reading customer data: " + e.getMessage());
        }
        return list;
    }

    public Customer getCustomerByID(String customerID) {
        for (Customer c : getAllCustomers()) {
            if (c.getCustomerID().equals(customerID)) {
                return c;
            }
        }
        return null;
    }

    public Customer getCustomerByLogin(String email, String password) {
        for (Customer c : getAllCustomers()) {
            if (c.getEmail().equals(email) && c.getPassword().equals(password)) {
                return c;
            }
        }
        return null;
    }
}