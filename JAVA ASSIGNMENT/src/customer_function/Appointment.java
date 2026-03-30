package customer_function;

import java.time.LocalDate;

public class Appointment {
    private String appointmentID;
    private String customerID;
    private String staffID;
    private String serviceTypeID;
    private String technicianID;
    private String serviceItemID;
    private LocalDate appointmentDate;
    private String serviceType;
    private String startTime;
    private String endTime;
    private String status;
    private String vehicleBrand;
    private String vehicleModel;
    private String vehiclePlate;
    private String command;

    public Appointment(String appointmentID, String customerID, String staffID, String serviceTypeID, 
        String technicianID, String serviceItemID, LocalDate appointmentDate, String startTime, 
        String endTime, String status, String vehicleBrand, String vehicleModel, String vehiclePlate, 
        String command, String serviceType) {

        this.appointmentID = appointmentID;
        this.customerID = customerID;
        this.staffID = staffID;
        this.serviceTypeID = serviceTypeID;
        this.technicianID = technicianID;
        this.serviceItemID = serviceItemID;
        this.appointmentDate = appointmentDate;
        this.serviceType = serviceType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.vehicleBrand = vehicleBrand;
        this.vehicleModel = vehicleModel;
        this.vehiclePlate = vehiclePlate;
        this.command = command;
    }

    public String getAppointmentID() {return appointmentID;}
    public String getCustomerID() {return customerID;}
    public String getStaffID() {return staffID;}
    public String getServiceTypeID() {return serviceTypeID;}
    public String getTechnicianID() {return technicianID;}
    public String getServiceItemID() {return serviceItemID;}
    public LocalDate getAppointmentDate() {return appointmentDate;}
    public String getStartTime() {return startTime;}
    public String getEndTime() {return endTime;}
    public String getStatus() {return status;}
    public String getVehicleBrand() {return vehicleBrand;}
    public String getVehicleModel() {return vehicleModel;}
    public String getVehiclePlate() {return vehiclePlate;}
    public String getCommand() {return command;}
    public String getServiceType() {return serviceType;}
}
