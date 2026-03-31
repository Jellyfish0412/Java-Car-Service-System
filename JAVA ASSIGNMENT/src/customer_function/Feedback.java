package customer_function;

public class Feedback {

    private String feedbackID;
    private String appointmentID;
    private String customerID;
    private String comment;
    private String rating;
    private String date;

    public Feedback(String feedbackID, String appointmentID, String customerID,
                    String comment, String rating, String date) {
        this.feedbackID    = feedbackID;
        this.appointmentID = appointmentID;
        this.customerID    = customerID;
        this.comment       = comment;
        this.rating        = rating;
        this.date          = date;
    }

    public String getFeedbackID()    { return feedbackID; }
    public String getAppointmentID() { return appointmentID; }
    public String getCustomerID()    { return customerID; }
    public String getComment()       { return comment; }
    public String getRating()        { return rating; }
    public String getDate()          { return date; }
}