package customer_function;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FeedbackDAO {
    private static final String FILE_PATH = "/Users/jellyfish/Desktop/AI Y2S1/OOP/JAVA ASSIGNMENT/Java-Car-Service-System/JAVA ASSIGNMENT/src/customer_data/feedback.txt";

    public Feedback getFeedbackByAppointmentID(String appointmentID) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\|", -1);
                if (parts.length >= 6 && parts[1].equals(appointmentID)) {
                    return new Feedback(
                        parts[0], 
                        parts[1], 
                        parts[2], 
                        parts[3], 
                        parts[4], 
                        parts[5]);
                }
                    
            }
        } catch (IOException e) {
            System.out.println("Cannot read feedback.txt: " + e.getMessage());
        }
        return null;
    }

    public List<Feedback> getFeedbackByCustomerID(String customerID) {
        List<Feedback> feedbacks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\|", -1);
                if (parts.length >= 6 && parts[2].equals(customerID)) {
                    feedbacks.add(new Feedback(
                        parts[0], 
                        parts[1], 
                        parts[2], 
                        parts[3], 
                        parts[4], 
                        parts[5]));
                }
            }
        } catch (IOException e) {
            System.out.println("Cannot read feedback.txt: " + e.getMessage());
        }
        return feedbacks;
    }

    public boolean submitFeedback(String appointmentID, String customerID, String ratings, String comments) {
        if (getFeedbackByAppointmentID(appointmentID) != null) {
            return false; // Feedback already exists for this appointment
        }

        String newID = generateNewID();
        String date = LocalDate.now().toString();
        String record   = newID + "|" + appointmentID + "|" + customerID + "|" + comments + "|" + ratings + "|" + date;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            bw.newLine();
            bw.write(record);
            return true;
        } catch (IOException e) {
            System.out.println("Cannot write to feedback.txt: " + e.getMessage());
            return false;

        }
    }

    private String generateNewID() {
        int max = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\|", -1);
                if (parts[0].startsWith("FB")) {
                    try {
                        int num = Integer.parseInt(parts[0].substring(2));
                        if (num > max) {
                            max = num;
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            }
        } catch (Exception e) {
        }
        return String.format("FB%03d", max + 1);
    }

}
