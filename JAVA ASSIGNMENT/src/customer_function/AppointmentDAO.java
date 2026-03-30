package customer_function;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    private static final String FILE_PATH = "/Users/jellyfish/Desktop/AI Y2S1/OOP/JAVA ASSIGNMENT/Java-Car-Service-System/JAVA ASSIGNMENT/src/customer_data/appointments.txt";

    public List<Appointment> getAllAppointments() {
        List<Appointment> apponitment = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(FILE_PATH));
            String line;

            while ((line = br.readLine()) != null) {
                String[] part = line.split("\\|");
                if (part.length == 15) {
                    Appointment a = new Appointment(
                        part[0], 
                        part[1], 
                        part[2], 
                        part[3], 
                        part[4], 
                        part[5], 
                        LocalDate.parse(part[6]), 
                        part[7], 
                        part[8],
                        part[9], 
                        part[10], 
                        part[11], 
                        part[12], 
                        part[13], 
                        part[14]);
                    apponitment.add(a);
            }
        }
        br.close();

        } catch (IOException e) {
            System.err.println("Error reading appointment data: " + e.getMessage());
        }
        return apponitment;
    }

    public Appointment getUpcomingAppointmentByCustomerID(String customerID) {
        LocalDate today = LocalDate.now();
        Appointment upcoming = null;

        for (Appointment a : getAllAppointments()) {
            if (a.getCustomerID().equals(customerID) && !a.getAppointmentDate().isBefore(today)) {
                if (upcoming == null || a.getAppointmentDate().isAfter(today)) {
                    upcoming = a;
                }
            }
        }
        return upcoming;
    }

    public List<Appointment> getPassAppointments(String customerID) {
        LocalDate today = LocalDate.now();
        List<Appointment> list = new ArrayList<>();

        for (Appointment a : getAllAppointments()) {
            if (a.getCustomerID().equals(customerID) && a.getAppointmentDate().isBefore(today)){
                list.add(a);
            }
        }
        return list;
    }


}
