package customer_function;

public class Testappointment {
    public static void main(String[] args) {

        AppointmentDAO dao = new AppointmentDAO();

        // Test 1: 读取所有 appointments
        System.out.println("=== All Appointments ===");
        for (Appointment a : dao.getAllAppointments()) {
            System.out.println(a.getAppointmentID() + " | " 
                             + a.getCustomerID() + " | " 
                             + a.getServiceType() + " | " 
                             + a.getStatus());
        }

        // Test 2: upcoming appointment
        System.out.println("\n=== Upcoming Appointment (C001) ===");
        System.out.println("Todays Date: " + java.time.LocalDate.now() + "\n");
        Appointment upcoming = dao.getUpcomingAppointmentByCustomerID("C001");
        if (upcoming != null) {
            System.out.println("Appointment ID : " + upcoming.getAppointmentID());
            System.out.println("Date           : " + upcoming.getAppointmentDate());
            System.out.println("Start Time     : " + upcoming.getStartTime());
            System.out.println("End Time       : " + upcoming.getEndTime());
            System.out.println("Service Type   : " + upcoming.getServiceType());
            System.out.println("Vehicle        : " + upcoming.getVehicleBrand() + " "
                             + upcoming.getVehicleModel() + " - "
                             + upcoming.getVehiclePlate());
            System.out.println("Status         : " + upcoming.getStatus());
            System.out.println("Command        : " + upcoming.getCommand());
        } else {
            System.out.println("No upcoming appointment found!");
        }

        // Test 3: past appointments
        System.out.println("\n=== Past Appointments (C001) ===");
        for (Appointment a : dao.getPassAppointments("C001")) {
            System.out.println(a.getAppointmentID() + " | "
                             + a.getAppointmentDate() + " | "
                             + a.getServiceType() + " | "
                             + a.getStatus());
        }

        // Test 4: submit new appointment
        System.out.println("\n=== Submitting New Appointment ===");
        boolean submitted = dao.submitAppointment("C001", "S001", java.time.LocalDate.now(), "10:00", "Toyota","Camry", "ABC-123", "Regular Maintenance");
        if (submitted) {
            System.out.println("New appointment submitted successfully!");
        } else {
            System.out.println("Failed to submit new appointment.");
        }
    }
}