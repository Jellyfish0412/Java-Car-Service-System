package customer_function;

public class TestTechnician {
    public static void main(String[] args) {

        TechnicianDAO dao = new TechnicianDAO();

        // Test 1: 读取所有 technicians
        System.out.println("=== All Technicians ===");
        for (Technician t : dao.getAllTechnicianList()) {
            System.out.println(t.getTechnicianID() + " | "
                             + t.getTechnicianName() + " | "
                             + t.getSpecialization() + " | "
                             + t.getStatus());
        }

        // Test 2: 用 ID 找 technician name
        System.out.println("\n=== Get Technician Name by ID ===");
        String name = dao.getTechnicianName("T001");
        if (name != null) {
            System.out.println("T001 Name : " + name);
        } else {
            System.out.println("Technician not found!");
        }

        // Test 3: ID 不存在
        System.out.println("\n=== Get Technician Name (invalid ID) ===");
        String invalid = dao.getTechnicianName("T999");
        if (invalid != null) {
            System.out.println("T999 Name : " + invalid);
        } else {
            System.out.println("Technician not found!");
        }
    }
}
