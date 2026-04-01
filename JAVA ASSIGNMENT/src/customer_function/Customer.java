package customer_function;

import java.time.LocalDate;

public class Customer {
	private String customerID;
    private String password;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private LocalDate registeredDate;

    public Customer(String customerID, String password, String name, 
        String email, String phoneNumber, String address, String registeredDate) {
        this.customerID = customerID;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.registeredDate = LocalDate.parse(registeredDate);
    }

    public String getCustomerID() {return customerID;}
    public String getPassword() {return password;}
    public String getName() {return name;}
    public String getEmail() {return email;}
    public String getPhoneNumber() {return phoneNumber;}
    public String getAddress() {return address;}
    public LocalDate getRegisteredDate() {return registeredDate;}

    public void setPassword(String password) {this.password = password;}
    public void setName(String name) {this.name = name;}
    public void setEmail(String email) {this.email = email;}
    public void setPhone(String phoneNumber) {this.phoneNumber = phoneNumber;}
    public void setAddress(String address) {this.address = address;}
    public void setCustomerID(String customerID2) {this.customerID = customerID2;}    
}
