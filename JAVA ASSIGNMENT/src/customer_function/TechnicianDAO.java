package customer_function;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TechnicianDAO {
    private static final String FILE_PATH = "/Users/jellyfish/Desktop/AI Y2S1/OOP/JAVA ASSIGNMENT/Java-Car-Service-System/JAVA ASSIGNMENT/src/customer_data/technician.txt";

    public List<Technician> getAllTechnicianList() {
        List<Technician> techList = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(FILE_PATH));
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 6) {
                    Technician t = new Technician(
                        parts[0], 
                        parts[1], 
                        parts[2], 
                        parts[3], 
                        parts[4], 
                        parts[5]);
                    techList.add(t);
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error reading technician data: " + e.getMessage());
        }
        return techList;
    }

    public String getTechnicianName(String technicianID) {
        for (Technician t : getAllTechnicianList()) {
            if (t.getTechnicianID().equals(technicianID)) {
                return t.getTechnicianName();
            }
        }
        return null;
    }
}
