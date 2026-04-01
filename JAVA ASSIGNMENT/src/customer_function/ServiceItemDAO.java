package customer_function;

import java.io.*;
import java.util.*;

public class ServiceItemDAO {

    private static final String FILE_PATH = "/Users/jellyfish/Desktop/AI Y2S1/OOP/JAVA ASSIGNMENT/Java-Car-Service-System/JAVA ASSIGNMENT/src/customer_data/service_items.txt";

    public List<ServiceItem> getAllServiceItems() {
        List<ServiceItem> list = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(FILE_PATH));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    ServiceItem s = new ServiceItem(
                        parts[0], 
                        parts[1], 
                        parts[2], 
                        parts[3], 
                        parts[4]  
                    );
                    list.add(s);
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error reading service_items.txt: " + e.getMessage());
        }
        return list;
    }

    public ServiceItem getServiceItemByID(String serviceItemID) {
        for (ServiceItem s : getAllServiceItems()) {
            if (s.getServiceItemID().equals(serviceItemID)) {
                return s;
            }
        }
        return null;
    }

    public List<ServiceItem> getServiceItemsByCategory(String category) {
        List<ServiceItem> list = new ArrayList<>();
        for (ServiceItem s : getAllServiceItems()) {
            if (s.getCategory().equals(category)) {
                list.add(s);
            }
        }
        return list;
    }

    public String getServiceTypeByServiceItemID(String serviceItemID) {
        ServiceItem s = getServiceItemByID(serviceItemID);
        if (s != null) {
            return s.getCategory();
        }
        return null;
    }

}