package customer_function;

public class Testserviceitem {
    public static void main(String[] args) {

        ServiceItemDAO dao = new ServiceItemDAO();

        System.out.println("=== All Service Items ===");
        for (ServiceItem s : dao.getAllServiceItems()) {
            System.out.println(s.getServiceItemID() + " | "
                             + s.getCategory() + " | "
                             + s.getDuration() + "hr | "
                             + s.getDescription());
        }

        System.out.println("\n=== Search by ID (SI001) ===");
        ServiceItem found = dao.getServiceItemByID("SI001");
        if (found != null) {
            System.out.println("ID       : " + found.getServiceItemID());
            System.out.println("Category : " + found.getCategory());
            System.out.println("Duration : " + found.getDuration() + " hr");
            System.out.println("Desc     : " + found.getDescription());
        } else {
            System.out.println("Not found!");
        }

        System.out.println("\n=== Normal Services ===");
        for (ServiceItem s : dao.getServiceItemsByCategory("Normal")) {
            System.out.println(s.getServiceItemID() + " | " + s.getDescription());
        }

        System.out.println("\n=== Special Services ===");
        for (ServiceItem s : dao.getServiceItemsByCategory("Special")) {
            System.out.println(s.getServiceItemID() + " | " + s.getDescription());
        }

        System.out.println("\n=== Get Category by Service Item ID (SI002) ===");
        String category = dao.getServiceTypeByServiceItemID("SI002");
        if (category != null) {
            System.out.println("Service Item SI002 belongs to category: " + category);
        } else {
            System.out.println("Service Item SI002 not found.");
        }
    }
}
