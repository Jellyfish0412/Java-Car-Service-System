package customer_function;

public class Technician {
    private String technicianID;
    private String technicianName;
    private String technicianEmail;
    private String technicianPassword;
    private String status;
    private String specialization;

    public Technician(String technicianID, String technicianName, String technicianEmail,
                      String technicianPassword, String status, String specialization) {
        this.technicianID = technicianID;
        this.technicianName = technicianName;
        this.technicianEmail = technicianEmail;
        this.technicianPassword = technicianPassword;
        this.status = status;
        this.specialization = specialization;
    }

    public String getTechnicianID() { return technicianID; }
    public String getTechnicianName() { return technicianName; }
    public String getTechnicianEmail() { return technicianEmail; }
    public String getTechnicianPassword() { return technicianPassword; }
    public String getStatus() { return status; }
    public String getSpecialization() { return specialization; }
}
