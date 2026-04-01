package customer_function;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    private static final String FILE_PATH = "/Users/jellyfish/Desktop/AI Y2S1/OOP/JAVA ASSIGNMENT/Java-Car-Service-System/JAVA ASSIGNMENT/src/customer_data/appointments.txt";

    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

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
                    appointments.add(a);
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading appointment data: " + e.getMessage());
        }
        return appointments;
    }

    // 修复：找最近的upcoming appointment（日期最小但不早于今天）
    public Appointment getUpcomingAppointmentByCustomerID(String customerID) {
        LocalDate today = LocalDate.now();
        Appointment upcoming = null;

        for (Appointment a : getAllAppointments()) {
            if (a.getCustomerID().equals(customerID) && !a.getAppointmentDate().isBefore(today)) {
                if (upcoming == null || a.getAppointmentDate().isBefore(upcoming.getAppointmentDate())) {
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
            if (a.getCustomerID().equals(customerID) && a.getAppointmentDate().isBefore(today)) {
                list.add(a);
            }
        }
        return list;
    }

    public boolean submitAppointment(String customerID, String serviceItemID, LocalDate date, String time, String vehicleBrand, String vehicleModel, String vehiclePlate, String command) {

        for (Appointment a : getAllAppointments()) {
            if (a.getCustomerID().equals(customerID)
                    && a.getAppointmentDate().equals(date)
                    && a.getStartTime().equals(time)) {
                System.out.println("Duplicate appointment detected. Submission rejected.");
                return false;
            }
        }

        String newID = generateNewAppointmentId();
        ServiceItemDAO serviceItemDAO = new ServiceItemDAO();
        String serviceType = serviceItemDAO.getServiceTypeByServiceItemID(serviceItemID);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            String line = String.format("%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s",
                newID,
                customerID,
                "N/A",
                "N/A",
                "N/A",
                serviceItemID,
                date.toString(),
                time,
                "N/A",
                "Pending",
                vehicleBrand,
                vehicleModel,
                vehiclePlate,
                command,
                serviceType);
            bw.newLine();
            bw.write(line);
            return true;
        } catch (IOException e) {
            System.out.println("Cannot write to appointments.txt: " + e.getMessage());
            return false;
        }
    }

    private String generateNewAppointmentId() {
        int max = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\|", -1);
                if (parts[0].startsWith("A")) {
                    try {
                        int num = Integer.parseInt(parts[0].substring(1));
                        if (num > max) {
                            max = num;
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading appointments for ID generation: " + e.getMessage());
        }
        return String.format("A%03d", max + 1);
    }
}